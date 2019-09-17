package com.github.whitepin.sdk.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.whitepin.sdk.contruct.FabricContruct;
import com.github.whitepin.sdk.whitepin.context.ConditionType;
import com.github.whitepin.sdk.whitepin.context.OrderType;
import com.github.whitepin.sdk.whitepin.context.ReportType;
import com.github.whitepin.sdk.whitepin.invocation.ChaincodeInvocation;
import com.github.whitepin.sdk.whitepin.invocation.ChaincodeInvocationImpl;
import com.github.whitepin.sdk.whitepin.vo.ScoreVo;
import com.github.whitepin.sdk.whitepin.vo.TradeVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo;

public class FabricChaincodeInvocationIT {

    private FabricContruct fabricContruct;
    private ChaincodeInvocation chaincodeInvocation;

    private Channel channel;
    private HFClient client;

    @BeforeEach
    void setUp() throws Exception {
        fabricContruct = new FabricContruct();
        fabricContruct.setUp();

        chaincodeInvocation = new ChaincodeInvocationImpl();

        this.channel = fabricContruct.getChannel();
        this.client = fabricContruct.getClient();
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자 추가 테스트")
    @ValueSource(strings = { "BB" })
    void addUserTest(String userTkn) throws Exception {
        boolean result = chaincodeInvocation.addUser(channel, client, userTkn);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자 조회 테스트")
    @ValueSource(strings = { "BB" })
    void queryUserTest(String userTkn) throws Exception {
        UserVo userVo = chaincodeInvocation.queryUser(channel, client, userTkn);
        assertThat(userVo.getUserTkn())
                .isEqualTo("BB");
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자 조회 테스트")
    @ValueSource(strings = { "TOTAL_USER" })
    void queryUserTotalTest(String userTkn) throws Exception {
        UserVo userVo = chaincodeInvocation.queryUser(channel, client, userTkn);
        System.out.println(userVo.toString());
        assertThat(userVo.getUserTkn())
                .isEqualTo("TOTAL_USER");
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("거래 생성 테스트")
    @CsvSource(value = { "AB01111,PTN0001,AA,BB" })
    void createTradeTest(String tradeId, String serviceCode, String sellerTkn, String buyerTkn)
            throws Exception {
        boolean result =
                chaincodeInvocation.createTrade(channel, client, tradeId, serviceCode, sellerTkn, buyerTkn);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("query string으로 거래 이력 조회 테스트")
    @ValueSource(strings = "{\"selector\":{\"RecType\":2,\"SellerTkn\":\"AA\"},\"sort\":[{\"Date\":\"desc\"}]}")
    void queryTradeWithQueryStringTest(String queryString) throws Exception {
        List<TradeVo> tradeVos = chaincodeInvocation.queryTradeWithQueryString(channel, client, queryString);

        assertThat(tradeVos.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자로 거래이력 조회. (판매, 구매, 모두 : sell, buy, all)")
    @CsvSource(value = { "AA,3,2,''" })
    //    @EnumSource(value = ConditionType.class, names = {})
    void queryTradeWithUserTest1(String userTkn, String pageSize, String pageNum, String bookmark)
            throws Exception {
        ConditionType conditionType = ConditionType.ALL;
        OrderType orderType = OrderType.ASC;
        ReportType reportType = ReportType.PAGE;
        List<TradeVo> tradeVos = chaincodeInvocation.queryTradeWithUser(channel, client, userTkn, conditionType,
                orderType, reportType, pageSize, pageNum, bookmark);

        assertThat(tradeVos.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자로 거래이력 조회. (판매, 구매, 모두 : sell, buy, all) report type :: page")
    @CsvSource(value = { "AA,3,2,''" })
    //    @EnumSource(value = ConditionType.class, names = {})
    void queryTradeWithUserTest2(String userTkn, String pageSize, String pageNum, String bookmark)
            throws Exception {
        ConditionType conditionType = ConditionType.ALL;
        OrderType orderType = OrderType.ASC;
        List<TradeVo> tradeVos = chaincodeInvocation.queryTradeWithUser(channel, client, userTkn, conditionType,
                orderType, pageSize, pageNum, bookmark);

        assertThat(tradeVos.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자, 서비스 코드로 거래이력 조회 테스트")
    @CsvSource(value = { "AA,service01,3,2,''" })
    void queryTradeWithUserServiceTest(String userTkn, String serviceCode, String pageSize, String pageNum,
            String bookmark) throws Exception {
        ConditionType conditionType = ConditionType.ALL;
        OrderType orderType = OrderType.ASC;
        ReportType reportType = ReportType.PAGE;

        List<TradeVo> tradeVos =
                chaincodeInvocation.queryTradeWithUserService(channel, client, userTkn, serviceCode,
                        conditionType, orderType, reportType, pageSize, pageNum, bookmark);
        assertThat(tradeVos.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("서비스 코드로 거래이력 조회 테스트")
    @CsvSource(value = { "service01,'','',''" })
    void queryTradeWithServiceTest(String serviceCode, String pageSize, String pageNum, String bookmark)
            throws Exception {
        OrderType orderType = OrderType.ASC;
        //TODO :: reportType이 page일 경우 작동하지 않음.. 체크 예정
        ReportType reportType = ReportType.NORMAL;

        List<TradeVo> tradeVos =
                chaincodeInvocation.queryTradeWithService(channel, client, serviceCode, orderType,
                        reportType, pageSize, pageNum, bookmark);
        assertThat(tradeVos.size()).isGreaterThan(0);
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("거래 조회 테스트")
    @ValueSource(strings = { "AB01" })
    void queryTradeWithIdTest(String tradeId) throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeWithId(channel, client, tradeId);

        assertThat(tradeVo.getTradeId()).isEqualTo("AB01");
    }

    @ParameterizedTest
//    @Disabled
    @DisplayName("score key로 점수 조회 테스트")
    @ValueSource(strings = { "AB01_ScoreTemp" })
    void queryScoreTempTest(String scoreKey) throws Exception {
        ScoreVo scoreVo = chaincodeInvocation.queryScoreTemp(channel, client, scoreKey);
        assertThat(scoreVo.getScoreKey()).isEqualTo("AB01_ScoreTemp");
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("Trade ID로 임시 평가정수 조회 테스트")
    @ValueSource(strings = { "AB01" })
    void queryScoreTempWithTradeIdTest(String tradeId) throws Exception {
        ScoreVo scoreVo = chaincodeInvocation.queryScoreTempWithTradeId(channel, client, tradeId);
        assertThat(scoreVo.getTradeId()).isEqualTo("AB01");
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("거래 완료 처리 테스트")
    @CsvSource(value = { "AB01,BB" })
    void closeTradeTest(String tradeId, String userTkn) throws Exception {
        boolean result = chaincodeInvocation.closeTrade(channel, client, tradeId, userTkn);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("판매자 또는 구매자 점수 등록 테스트")
    @CsvSource(value = { "AB01,BB,key1234" })
    void enrollTempScoreTest(String tradeId, String userTkn, String key) throws Exception {
        String scoreOrigin = Arrays.asList("4", "4", "4").toString();
        boolean result =
                chaincodeInvocation.enrollTempScore(channel, client, tradeId, userTkn, scoreOrigin, key);
        assertThat(result).isTrue();
    }
    
    @ParameterizedTest
//    @Disabled
    @DisplayName("판매자 또는 구매자 점수 등록 테스트 - key 값 고정")
    @CsvSource(value = { "AB01,BB" })
    void enrollTempScoreTest(String tradeId, String userTkn) throws Exception {
        String scoreOrigin = Arrays.asList("4", "4", "4").toString();
        boolean result =
                chaincodeInvocation.enrollTempScore(channel, client, tradeId, userTkn, scoreOrigin);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("판매자 구매자 동시에 거래 점수 등록 테스트")
    @CsvSource(value = { "EA05,key1234" })
    void enrollScoreTest(String tradeId, String key) throws Exception {
        boolean result = chaincodeInvocation.enrollScore(channel, client, tradeId, key);
        assertThat(result).isTrue();
    }

}
