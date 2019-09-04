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

import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "password", "enrollment", "enrollmentSecret" })
public class FabricUserContext implements User {

    /**
     * 기본 사용자 이름
     */
    public static final String DEFAULT_USER_NAME = "FabricUser";

    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private String mspId;
    private String password;

    private String enrollmentSecret;
    private Enrollment enrollment;
    private boolean isAdmin;

    @Builder
    public FabricUserContext(String name, Set<String> roles, String account, String affiliation,
                             String mspId, String password, String enrollmentSecret,
                             Enrollment enrollment, boolean isAdmin) {
        this.name = name;
        this.roles = roles;
        this.account = account;
        this.affiliation = affiliation;
        this.mspId = mspId;
        this.password = password;
        this.enrollmentSecret = enrollmentSecret;
        this.enrollment = enrollment;
        this.isAdmin = isAdmin;
    }
}
