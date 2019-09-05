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

/*
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

package com.github.whitepin.sdk.client;

import java.util.Properties;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import com.github.whitepin.sdk.context.FabricUserContext;
import com.github.whitepin.sdk.exception.FabricClientCreateException;

/**
 * Fabric client 및 ca client factory
 */
public class FabricClientFactory {

    /**
     * HFCAClient 인스턴스 생성
     *
     * @param caName     : ca 이름 (e.g : ca0.testnet.com)
     * @param protocol   : http or https
     * @param address    : ca server host
     * @param port       : ca server port
     * @param properties : properties
     */
    public HFCAClient createCaClient(String caName, String protocol, String address, Integer port,
                                     Properties properties) throws FabricClientCreateException {

        return createCaClient(caName, protocol + "://" + address + ":" + port, properties);
    }

    /**
     * HFCAClient 인스턴스 생성 메소드
     *
     * @param caName     : ca 이름 (e.g : ca0.testnet.com)
     * @param caLocation : ca 주소 (http://192.168.10.11:7054)
     */
    public HFCAClient createCaClient(String caName, String caLocation)
            throws FabricClientCreateException {

        return createCaClient(caName, caLocation, null);
    }

    /**
     * HFCAClient 인스턴스 생성 메소드
     *
     * @param caName     : ca 이름 (e.g : ca0.testnet.com)
     * @param caLocation : ca 주소 (http://192.168.10.11:7054)
     * @param properties : ca properties (tls 등)
     */
    public HFCAClient createCaClient(String caName, String caLocation, Properties properties)
            throws FabricClientCreateException {

        CryptoSuite cryptoSuite = null;

        try {
            cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        } catch (Exception e) {
            throw new FabricClientCreateException(e);
        }

        return createCaClient(caName, caLocation, properties, cryptoSuite);
    }

    public HFCAClient createCaClient(String caName, String caLocation, Properties properties,
                                     CryptoSuite cryptoSuite) throws FabricClientCreateException {

        try {
            HFCAClient caClient = HFCAClient.createNewInstance(caName, caLocation, properties);
            caClient.setCryptoSuite(cryptoSuite);
            return caClient;
        } catch (Exception e) {
            throw new FabricClientCreateException(e);
        }
    }

    /**
     * 기본 HFClient 생성
     */
    public HFClient createHFClient() throws FabricClientCreateException {
        return createHFClient(null);
    }

    /**
     * 기본 HFClient + user context 생성
     */
    public HFClient createHFClient(FabricUserContext fabricUserContext)
            throws FabricClientCreateException {

        try {
            CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

            HFClient client = HFClient.createNewInstance();
            client.setCryptoSuite(cryptoSuite);

            if (fabricUserContext != null) {
                client.setUserContext(fabricUserContext);
            }

            return client;
        } catch (Exception e) {
            throw new FabricClientCreateException(e);
        }
    }
}
