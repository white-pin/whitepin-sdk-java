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
import java.util.Optional;

import com.github.whitepin.sdk.context.FabricCAContext;
import com.github.whitepin.sdk.context.FabricOrdererContext;
import com.github.whitepin.sdk.context.FabricOrgContext;
import com.github.whitepin.sdk.context.FabricPeerContext;

/**
 * Fabric components factory such as ca, orderer, peer, channel
 */
public interface FabricComponentFactory {

    /**
     * Returns {@link Optional} containing {@link FabricCAContext} given ca name.
     */
    Optional<FabricCAContext> getCaContext(String caName);

    /**
     * Returns {@link Optional} containing {@link FabricOrdererContext} given orderer name.
     */
    Optional<FabricOrdererContext> getOrdererContext(String ordererName);

    /**
     * Returns {@link Optional} containing {@link FabricPeerContext} given peer name.
     */
    Optional<FabricPeerContext> getPeerContext(String peerName);

    /**
     * Returns {@link Optional} containing {@link FabricOrgContext} given org name.
     */
    Optional<FabricOrgContext> getOrgContext(String orgName);

    /**
     * Returns {@link Optional} containing{@link Channel} given channel name.
     */
    Optional<Channel> getChannel(String channelName);
}
