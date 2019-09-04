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

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Fabric orderer 노드 context
 */
@Getter
@Setter
@NoArgsConstructor
public class FabricOrdererContext {

    private String name;            // orderer 이름
    private String location;        // orderer 주소 e.g) grpc://localhost:7050
    private Properties properties;  // orderer properties

    /**
     * Orderer grpc 통신 관련 기본 값 설정
     * - grpc.NettyChannelBuilderOption.keepAliveTime={ 5L, TimeUnit.MINUTES }
     * - grpc.NettyChannelBuilderOption.keepAliveTimeout={ 8L, TimeUnit.SECONDS }
     * - grpc.NettyChannelBuilderOption.keepAliveWithoutCalls={ true }
     */
    public static Properties appendDefaultProperties(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }

        Object[] keyValues = {
                "grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] { 5L, TimeUnit.MINUTES }
                , "grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] { 8L, TimeUnit.SECONDS }
                , "grpc.NettyChannelBuilderOption.keepAliveWithoutCalls", new Object[] { true }
        };

        for (int i = 0; i < keyValues.length; i += 2) {
            if (properties.get(keyValues[i]) == null) {
                properties.put(keyValues[i], keyValues[i + 1]);
            }
        }

        return properties;
    }

    @Builder
    public FabricOrdererContext(String name, String location, Properties properties) {
        this.name = name;
        this.location = location;
        this.properties = properties;
    }
}
