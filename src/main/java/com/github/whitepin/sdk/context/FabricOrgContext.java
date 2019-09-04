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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Fabric 조직 관련 context
 */
@Getter
@Setter
@NoArgsConstructor
public class FabricOrgContext {

    private FabricOrgType orgType;
    private String name;
    private String domain;

    private Map<String, FabricUserContext> users = new HashMap<>();
    private Map<String, FabricPeerContext> peers = new HashMap<>();
    private Map<String, FabricOrdererContext> orderers = new HashMap<>();

    @Builder
    public FabricOrgContext(FabricOrgType orgType, String name, String domain) {
        this.orgType = orgType;
        this.name = name;
        this.domain = domain;
    }

    /**
     * Fabric user context 추가
     */
    public void addUser(String name, FabricUserContext userContext) {
        synchronized (users) {
            users.put(name, userContext);
        }
    }

    /**
     * Fabric peer context 추가
     */
    public void addPeer(String name, FabricPeerContext peerContext) {
        synchronized (peers) {
            peers.put(name, peerContext);
        }
    }

    /**
     * Fabric orderer context 추가
     */
    public void addOrderer(String name, FabricOrdererContext ordererContext) {
        synchronized (orderers) {
            orderers.put(name, ordererContext);
        }
    }

    /**
     * Fabric admin 반환
     *
     * @throws IllegalStateException admin이 1개 이상 존재 할 경우 예외 전가
     */
    public FabricUserContext getAdmin() {
        synchronized (users) {
            List<FabricUserContext> admin = users.values()
                                                 .stream()
                                                 .filter(user -> user.isAdmin())
                                                 .collect(Collectors.toList());

            if (admin.size() != 1) {
                throw new IllegalStateException("Admin must be one but exist "
                                                + admin.size() + " admins.");
            }

            return admin.get(0);
        }
    }
}
