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

package com.github.whitepin.sdk.context;

import java.util.EnumSet;
import java.util.Properties;

import org.hyperledger.fabric.sdk.Peer.PeerRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Fabric peer 노드 context
 */
@Getter
@Setter
@NoArgsConstructor
public class FabricPeerContext {

    private String name;                    // peer 이름
    private String location;                // peer 주소 e.g) grpc://localhost:7051
    private Properties properties;          // peer properties
    private String hostAndPort;             // peer host e.g) peer0.peerorg1.testnet.com:7051
    private EnumSet<PeerRole> peerRoles;    // peer roles

    /**
     * Peer grpc 통신 관련 기본 값 설정
     * - grpc.NettyChannelBuilderOption.maxInboundMessageSize==9000000
     */
    public static Properties appendDefaultProperties(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }

        Object[] keyValues = {
                "grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000
        };

        for (int i = 0; i < keyValues.length; i += 2) {
            if (properties.get(keyValues[i]) == null) {
                properties.put(keyValues[i], keyValues[i + 1]);
            }
        }

        return properties;
    }

    /**
     * 기본 Peer role 반환
     */
    public static EnumSet<PeerRole> createDefaultPeerRoles() {
        return EnumSet.of(PeerRole.ENDORSING_PEER, PeerRole.LEDGER_QUERY,
                          PeerRole.CHAINCODE_QUERY, PeerRole.EVENT_SOURCE);
    }

    @Builder
    public FabricPeerContext(String name, String location, Properties properties, String hostAndPort,
                             EnumSet<PeerRole> peerRoles) {
        this.name = name;
        this.location = location;
        this.properties = properties;
        this.hostAndPort = hostAndPort;
        this.peerRoles = peerRoles;
    }
}
