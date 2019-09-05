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

package com.github.whitepin.sdk.common;

public class TestHelper {

    public static void out(String format, Object... args) {
        System.err.flush();
        System.out.flush();

        System.out.println(String.format(format, args));

        System.out.flush();
        System.err.flush();
    }
}
