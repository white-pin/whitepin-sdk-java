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

package com.github.whitepin.sdk.whitepin.vo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ScoreVoTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private String jsonValue = "{ "
                               + "\"RecType\": 3, "
                               + "\"ScoreKey\": \"AB01_ScoreTemp\", "
                               + "\"TradeId\": \"AB01\", "
                               + "\"ExpiryDate\": \"2019-09-19T00:28:48.9594582Z\", "
                               + "\"Score\": { \"SellScore\": \"\", \"BuyScore\": \"\" },"
                               + " \"IsExpired\": true "
                               + "}";

    private String jsonValue2 = "{ "
                                + "\"RecType\": 3, "
                                + "\"ScoreKey\": \"AB01_ScoreTemp\", "
                                + "\"ExpiryDate\": \"2019-09-19T00:28:48.9594582Z\", "
                                + "\"Score\": { \"SellScore\": \"\", \"BuyScore\": \"\" }"
                                + "}";

    @Test
    public void testDeserialize() throws Exception {
        // when
        ScoreVo score = objectMapper.readValue(jsonValue, ScoreVo.class);

        // then
        assertThat(score).isNotNull();
        assertThat(score.getRecType()).isEqualTo(3);
        assertThat(score.getScoreKey()).isEqualTo("AB01_ScoreTemp");
        assertThat(score.getTradeId()).isEqualTo("AB01");
        assertThat(score.getExpiryDate()).isEqualTo("2019-09-19T00:28:48.9594582Z");
        assertThat(score.isExpired()).isTrue();
        assertThat(score.getScore()).isNotNull();

        // when
        score = objectMapper.readValue(jsonValue2, ScoreVo.class);

        // then
        assertThat(score).isNotNull();
        assertThat(score.getRecType()).isEqualTo(3);
        assertThat(score.getScoreKey()).isEqualTo("AB01_ScoreTemp");
        assertThat(score.getTradeId()).isNull();
        assertThat(score.getExpiryDate()).isEqualTo("2019-09-19T00:28:48.9594582Z");
        assertThat(score.isExpired()).isFalse();
        assertThat(score.getScore()).isNotNull();
    }
}
