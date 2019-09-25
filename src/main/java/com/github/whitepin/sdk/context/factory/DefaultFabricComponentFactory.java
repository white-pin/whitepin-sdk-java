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

package com.github.whitepin.sdk.context.factory;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.whitepin.sdk.context.FabricCAContext;
import com.github.whitepin.sdk.context.FabricOrdererContext;
import com.github.whitepin.sdk.context.FabricOrgContext;
import com.github.whitepin.sdk.context.FabricPeerContext;

/**
 * Default fabric component factory.
 */
public class DefaultFabricComponentFactory implements FabricComponentFactory {

    private Map<String, FabricCAContext> caMap = new HashMap<>();
    private Map<String, FabricOrgContext> orgs = new HashMap<>();
    private Map<String, FabricPeerContext> peers = new HashMap<>();
    private Map<String, FabricOrdererContext> orderers = new HashMap<>();
    private Map<String, Channel> channels = new HashMap<>();

    /**
     * Build a fabric component factory.
     *
     * @throws IllegalStateException if have invalid config contents.
     */
    public DefaultFabricComponentFactory(String configYaml) {
        throw new UnsupportedOperationException("Not supported yet");
        // initialize(configYaml);
    }

    @Override
    public Optional<FabricCAContext> getCaContext(String caName) {
        return Optional.ofNullable(caMap.get(caName));
    }

    @Override
    public Optional<FabricOrdererContext> getOrdererContext(String ordererName) {
        return Optional.ofNullable(orderers.get(ordererName));
    }

    @Override
    public Optional<FabricPeerContext> getPeerContext(String peerName) {
        return Optional.ofNullable(peers.get(peerName));
    }

    @Override
    public Optional<FabricOrgContext> getOrgContext(String orgName) {
        return Optional.ofNullable(orgs.get(orgName));
    }

    @Override
    public Optional<Channel> getChannel(String channelName) {
        return Optional.ofNullable(channels.get(channelName));
    }

    /**
     * Initialize fabric components from config yaml.
     */
    private void initialize(String configYaml) {
        // YAMLMapper objectMapper = new ObjectMapper();
    }
}
