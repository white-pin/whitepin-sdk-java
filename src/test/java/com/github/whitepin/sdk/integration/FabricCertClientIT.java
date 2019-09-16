/*
 * Copyright 2019 White-pin Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.whitepin.sdk.integration;

import static com.github.whitepin.sdk.constant.FabricSdkConstants.ADMIN_ATTRIBUTES;
import static com.github.whitepin.sdk.constant.FabricSdkConstants.CLIENT_IDENTITY_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.HFCAAffiliation;
import org.hyperledger.fabric_ca.sdk.HFCACertificateRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAIdentity;
import org.hyperledger.fabric_ca.sdk.HFCAX509Certificate;
import org.junit.Before;
import org.junit.Test;

import com.github.whitepin.sdk.client.FabricCertClient;
import com.github.whitepin.sdk.client.FabricClientFactory;
import com.github.whitepin.sdk.common.TestHelper;
import com.github.whitepin.sdk.context.FabricUserContext;

/**
 * Integration test of FabricCertClient
 */
public class FabricCertClientIT {

    FabricCertClient certClient;
    HFCAClient caClient;
    FabricUserContext caAdmin;
    FabricClientFactory clientFactory;

    @Before
    public void setUp() throws Exception {
        String name = "ca0";
        String url = "https://" + TestConstants.LOCAL_HOST + ":7054";
        Properties properties = new Properties();
        File certFile = new File("src/test/fixture/certintegration/ca-msp/ca-cert.pem");
        properties.setProperty("pemFile", certFile.getAbsolutePath());
        properties.setProperty("allowAllHostNames", "true"); //testing environment only NOT FOR PRODUCTION!
        clientFactory = new FabricClientFactory();

        caClient = clientFactory.createCaClient(name, url, properties);

        certClient = new FabricCertClient();

        caAdmin = FabricUserContext.builder()
                                   .name("admin@RootCA")
                                   .enrollmentSecret("adminpw")
                                   .build();

        Enrollment enroll = caClient.enroll(caAdmin.getName(), caAdmin.getEnrollmentSecret());
        assertThat(enroll).isNotNull();
        caAdmin.setEnrollment(enroll);
    }

    @Test
    public void runTests() throws Exception {
        FabricUserContext peerorg1Admin = FabricUserContext.builder()
                                                           .name("peerorg1Admin")
                                                           .password("passpw")
                                                           .affiliation("peerorg1")
                                                           .build();

        // 1) Check affiliation "peerorg1" > register
        Optional<HFCAAffiliation> affOptional = certClient.getAffiliationByName(caClient,
                                                                                caAdmin.getEnrollment(),
                                                                                peerorg1Admin.getAffiliation());

        assertThat(affOptional.isPresent()).isFalse();
        certClient.createNewAffiliation(caClient, caAdmin.getEnrollment(), peerorg1Admin.getAffiliation());

        // 2) Check identity > register
        Optional<HFCAIdentity> identityOptional = certClient.getIdentityByName(caClient,
                                                                               caAdmin.getEnrollment(),
                                                                               peerorg1Admin.getName());
        assertThat(identityOptional.isPresent()).isFalse();
        boolean result = certClient.registerNewIdentity(caClient, caAdmin.getEnrollment(),
                                                        CLIENT_IDENTITY_TYPE,
                                                        peerorg1Admin.getName(),
                                                        peerorg1Admin.getPassword(),
                                                        peerorg1Admin.getAffiliation(), ADMIN_ATTRIBUTES);
        assertThat(result).isTrue();
        peerorg1Admin.setEnrollmentSecret(peerorg1Admin.getPassword());

        // 3) Check enrollment > enroll
        HFCACertificateRequest requestFilter = caClient.newHFCACertificateRequest();
        requestFilter.setEnrollmentID(peerorg1Admin.getName());
        List<HFCAX509Certificate> certs = certClient.getCertificates(caClient, caAdmin.getEnrollment(),
                                                                     requestFilter);
        assertThat(certs).isEmpty();
        Enrollment enrollment = certClient.enroll(caClient, peerorg1Admin.getName(),
                                                  peerorg1Admin.getEnrollmentSecret());

        assertThat(enrollment).isNotNull();
        peerorg1Admin.setEnrollment(enrollment);

        // 5) reenroll
        Enrollment tempCert = caClient.reenroll(peerorg1Admin);
        assertThat(tempCert).isNotNull();

        // 4) revoke & assert
        requestFilter = caClient.newHFCACertificateRequest();
        requestFilter.setEnrollmentID(peerorg1Admin.getName());
        requestFilter.setRevoked(false);

        assertThat(certClient.getCertificates(caClient, peerorg1Admin.getEnrollment(), requestFilter).size())
                .isEqualTo(2);

        caClient.revoke(caAdmin, tempCert, "Reason from revoke request");

        requestFilter = caClient.newHFCACertificateRequest();
        requestFilter.setEnrollmentID(peerorg1Admin.getName());
        requestFilter.setRevoked(false);

        assertThat(certClient.getCertificates(caClient, peerorg1Admin.getEnrollment(), requestFilter).size())
                .isEqualTo(1);

        // 5) reenroll
        Enrollment reenroll = caClient.reenroll(peerorg1Admin);
        assertThat(reenroll).isNotNull();
    }
}
