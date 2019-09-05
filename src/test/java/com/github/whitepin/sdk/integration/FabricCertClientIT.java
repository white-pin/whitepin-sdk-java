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
 * Fabric cert client 통합 테스트
 */
public class FabricCertClientIT {

    FabricCertClient certClient;
    HFCAClient caClient;
    FabricUserContext caAdmin;
    FabricClientFactory clientFactory;

    @Before
    public void setUp() throws Exception {
        String name = "ca0";
        String url = "https://127.0.0.1:7054";
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

        TestHelper.out("> Try to register ca admin..");
        Enrollment enroll = caClient.enroll(caAdmin.getName(), caAdmin.getEnrollmentSecret());
        caAdmin.setEnrollment(enroll);
        TestHelper.out("> Success to register..");
    }

    /**
     * - affiliation 추가
     * - identity
     * - enroll
     * - reenroll
     * - revoke
     * - reenroll
     */
    @Test
    public void runTests() throws Exception {
        FabricUserContext peerorg1Admin = FabricUserContext.builder()
                                                           .name("peerorg1Admin")
                                                           .password("passpw")
                                                           .affiliation("peerorg1")
                                                           .build();

        // 1) affiliation 추가
        TestHelper.out("## Check affilication > %s", peerorg1Admin.getAffiliation());
        Optional<HFCAAffiliation> affOptional = certClient.getAffiliationByName(caClient,
                                                                                caAdmin.getEnrollment(),
                                                                                peerorg1Admin.getAffiliation());

        if (!affOptional.isPresent()) {
            TestHelper.out("> Try to create new affiliation");
            certClient.createNewAffiliation(caClient, caAdmin.getEnrollment(), peerorg1Admin.getAffiliation());
        } else {
            TestHelper.out("> Already exist");
        }

        // 2) identity 추가
        TestHelper.out("## Try to check identity %s", peerorg1Admin.getName());
        Optional<HFCAIdentity> identityOptional = certClient.getIdentityByName(caClient,
                                                                               caAdmin.getEnrollment(),
                                                                               peerorg1Admin.getName());
        if (!identityOptional.isPresent()) {
            TestHelper.out("> not exist. will register");
            boolean result = certClient.registerNewIdentity(caClient, caAdmin.getEnrollment(),
                                                            CLIENT_IDENTITY_TYPE,
                                                            peerorg1Admin.getName(),
                                                            peerorg1Admin.getPassword(),
                                                            peerorg1Admin.getAffiliation(), ADMIN_ATTRIBUTES);

            if (result) {
                peerorg1Admin.setEnrollmentSecret(peerorg1Admin.getPassword());
            }

            TestHelper.out(">> Identity result > " + result);
        } else {
            TestHelper.out("> Already exist identity");
            peerorg1Admin.setEnrollmentSecret(peerorg1Admin.getPassword());
        }

        // 3) enrollment
        TestHelper.out("## Try to check enrollments with filter with enrollment id : %s",
                       peerorg1Admin.getName());
        HFCACertificateRequest requestFilter = caClient.newHFCACertificateRequest();
        requestFilter.setEnrollmentID(peerorg1Admin.getName());
        List<HFCAX509Certificate> certs = certClient.getCertificates(caClient, caAdmin.getEnrollment(),
                                                                     requestFilter);
        if (!certs.isEmpty()) {
            TestHelper.out("> Already exist certs. size : %d", certs.size());
        }

        TestHelper.out("> Try to enroll %s(%s)", peerorg1Admin.getName(), peerorg1Admin.getEnrollmentSecret());
        Enrollment enrollment = certClient.enroll(caClient, peerorg1Admin.getName(),
                                                  peerorg1Admin.getEnrollmentSecret());

        peerorg1Admin.setEnrollment(enrollment);
        TestHelper.out(">> Success to enroll");
        TestHelper.out(peerorg1Admin.getEnrollment().getCert());

        // 5) reenroll
        TestHelper.out("## Try to reenroll cert");
        Enrollment tempCert = caClient.reenroll(peerorg1Admin);
        TestHelper.out("> Success to reenroll");

        // 4) revoke
        caClient.revoke(caAdmin, tempCert, "Reason from revoke request");
        TestHelper.out("> Success to revoke..");

        // 5) reenroll
        TestHelper.out("## Try to reenroll after revoke another cert");
        Enrollment reenroll = caClient.reenroll(peerorg1Admin);
        TestHelper.out("> Success to reenroll");
    }

}
