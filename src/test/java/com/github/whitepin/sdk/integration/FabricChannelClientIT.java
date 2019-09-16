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

import static org.assertj.core.api.Assertions.assertThat;

import com.github.whitepin.sdk.client.FabricChannelClient;
import com.github.whitepin.sdk.client.FabricClientFactory;
import com.github.whitepin.sdk.context.FabricOrdererContext;
import com.github.whitepin.sdk.context.FabricOrgContext;
import com.github.whitepin.sdk.context.FabricOrgType;
import com.github.whitepin.sdk.context.FabricPeerContext;
import com.github.whitepin.sdk.context.FabricUserContext;
import com.github.whitepin.sdk.parser.FabricCertParser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for FabricChannelClient
 */
public class FabricChannelClientIT {

    // channel
    static final String CHANNEL1 = "channel1";

    // fabric component
    static final String HOST = TestConstants.LOCAL_HOST;
    static final String ORDERER1 = "orderer1";
    static final String PEER1 = "peer1";
    static final String PEER2 = "peer2";
    static final String ADMIN = "Admin";

    FabricClientFactory clientFactory = new FabricClientFactory();
    FabricChannelClient channelClient = new FabricChannelClient();

    FabricOrgContext ordererorg1;
    FabricOrgContext peerorg1;
    FabricOrgContext peerorg2;

    byte[] channel1Config;

    @Before
    public void setUp() throws Exception {
        setUpContexts();
        setUpMaterials();
    }

    @Test
    public void runTests() throws Exception {
        /**
         * Create a channel "channel1" to orderers
         */
        HFClient client = clientFactory.createHFClient(peerorg1.getAdmin());
        Channel channel1 = channelClient.createChannel(
                client, CHANNEL1, channel1Config,
                Arrays.asList(
                        ordererorg1.getOrderers().get(ORDERER1)
                ),
                Arrays.asList(
                        peerorg1.getAdmin(), peerorg2.getAdmin()
                )
        );

        assertThat(channel1.isInitialized()).isFalse();

        /**
         * Request to join channel for [peer1.peerorg1, peer2.peerorg1, peer1.peerorg1]
         */
        channel1 = channelClient.joinPeer(client, channel1, peerorg1.getPeers().get(PEER1));
        channel1 = channelClient.joinPeer(client, channel1, peerorg1.getPeers().get(PEER2));

        client.setUserContext(peerorg2.getAdmin());
        channel1 = channelClient.joinPeer(client, channel1, peerorg2.getPeers().get(PEER1));
        channel1.initialize();
        assertThat(channel1.isInitialized()).isTrue();

        /**
         * Create a Channel instance for created already
         */
        assertThat(client.getChannel(CHANNEL1)).isNotNull();
        client = clientFactory.createHFClient(peerorg1.getAdmin());
        assertThat(client.getChannel(CHANNEL1)).isNull();

        channel1 = channelClient.buildChannel(
                client, CHANNEL1,
                Arrays.asList(
                        ordererorg1.getOrderers().get(ORDERER1)
                ),
                Arrays.asList(
                        peerorg1.getPeers().get(PEER1),
                        peerorg1.getPeers().get(PEER2),
                        peerorg2.getPeers().get(PEER1)
                )
        );
        assertThat(channel1.isInitialized()).isFalse();
        channel1.initialize();
        assertThat(channel1.isInitialized()).isTrue();
    }

    private void setUpMaterials() throws Exception {
        channel1Config = IOUtils.toByteArray(
                new File("src/test/fixture/channelintegration/configtx/channel1.tx").toURI()
        );
    }

    private void setUpContexts() throws Exception {
        /**
         * ordererorg1
         */
        ordererorg1 = FabricOrgContext.builder()
                                      .domain("ordererorg1.example.com")
                                      .name("ordererorg1")
                                      .orgType(FabricOrgType.ORDERER)
                                      .build();

        ordererorg1.addOrderer(
                ORDERER1,
                FabricOrdererContext.builder()
                                    .name("orderer1." + ordererorg1.getDomain())
                                    .location("grpc://" + HOST + ":7050")
                                    .properties(FabricOrdererContext.appendDefaultProperties(null))
                                    .build()
        );

        ordererorg1.addUser(ADMIN,
                            FabricUserContext.builder()
                                             .name("Admin")
                                             .affiliation("ordererorg1")
                                             .mspId("ordererorg1")
                                             .isAdmin(true)
                                             .orgType(FabricOrgType.ORDERER)
                                             .enrollment(
                                                     readEnrollment(FabricOrgType.ORDERER,
                                                                    ordererorg1.getDomain(), ADMIN)
                                             )
                                             .build()
        );

        /**
         * peerorg1
         */
        peerorg1 = FabricOrgContext.builder()
                                   .domain("peerorg1.example.com")
                                   .name("peerorg1")
                                   .orgType(FabricOrgType.PEER)
                                   .build();

        peerorg1.addPeer(
                PEER1,
                FabricPeerContext.builder()
                                 .name(PEER1)
                                 .location("grpc://" + HOST + ":7051")
                                 .properties(FabricPeerContext.appendDefaultProperties(null))
                                 .hostAndPort("peer1." + peerorg1.getDomain() + ":7051")
                                 .peerRoles(FabricPeerContext.createDefaultPeerRoles())
                                 .build()
        );

        peerorg1.addPeer(
                PEER2,
                FabricPeerContext.builder()
                                 .name(PEER2)
                                 .location("grpc://" + HOST + ":7056")
                                 .properties(FabricPeerContext.appendDefaultProperties(null))
                                 .hostAndPort("peer2." + peerorg1.getDomain() + ":7056")
                                 .peerRoles(FabricPeerContext.createDefaultPeerRoles())
                                 .build()
        );

        peerorg1.addUser(ADMIN,
                         FabricUserContext.builder()
                                          .name("Admin")
                                          .affiliation("peerorg1")
                                          .mspId("peerorg1")
                                          .isAdmin(true)
                                          .orgType(FabricOrgType.PEER)
                                          .enrollment(
                                                  readEnrollment(FabricOrgType.PEER, peerorg1.getDomain(),
                                                                 ADMIN)
                                          )
                                          .build()
        );

        /**
         * peerorg1
         */
        peerorg2 = FabricOrgContext.builder()
                                   .domain("peerorg2.example.com")
                                   .name("peerorg2")
                                   .orgType(FabricOrgType.PEER)
                                   .build();

        peerorg2.addPeer(
                PEER1,
                FabricPeerContext.builder()
                                 .name(PEER1)
                                 .location("grpc://" + HOST + ":8051")
                                 .properties(FabricPeerContext.appendDefaultProperties(null))
                                 .hostAndPort("peer1." + peerorg2.getDomain() + ":8051")
                                 .peerRoles(FabricPeerContext.createDefaultPeerRoles())
                                 .build()
        );

        peerorg2.addUser(ADMIN,
                         FabricUserContext.builder()
                                          .name("Admin")
                                          .affiliation("peerorg2")
                                          .mspId("peerorg2")
                                          .isAdmin(true)
                                          .orgType(FabricOrgType.PEER)
                                          .enrollment(
                                                  readEnrollment(FabricOrgType.PEER, peerorg2.getDomain(),
                                                                 ADMIN)
                                          )
                                          .build()
        );
    }

    private Enrollment readEnrollment(FabricOrgType orgType, String org, String name)
            throws Exception {

        String dirPath = "src/test/fixture/channelintegration/crypto-config/{ORG_TYPE}"
                         + "/{org}/users/{name}@{org}/msp";

        String orgTypeValue = orgType == FabricOrgType.ORDERER
                              ? "ordererOrganizations" : "peerOrganizations";

        String replaced = dirPath.replace("{ORG_TYPE}", orgTypeValue)
                                 .replace("{org}", org)
                                 .replace("{name}", name);

        Path mspPath = Paths.get(replaced);

        // 1) keystore
        File[] keyFiles = mspPath.resolve("keystore").toFile().listFiles();
        if (keyFiles.length != 1) {
            throw new IllegalStateException("Failed to read enrollment because multiple key file."
                                            + "org : " + org + ", name : " + name);
        }

        byte[] key = IOUtils.toByteArray(keyFiles[0].toURI());

        // 2) signcerts
        File[] certFiles = mspPath.resolve("signcerts").toFile().listFiles();
        if (certFiles.length != 1) {
            throw new IllegalStateException("Failed to read enrollment because multiple cert file."
                                            + "org : " + org + ", name : " + name);
        }

        byte[] cert = IOUtils.toByteArray(certFiles[0].toURI());

        return FabricCertParser.x509EnrollmentOf(key, cert);
    }
}
