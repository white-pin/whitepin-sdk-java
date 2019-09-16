package com.github.whitepin.sdk.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

    private String userTkn;
    private String tradeId;
    private String serviceCode;
    private String sellerTkn;
    private String buyerTkn;
    private String scoreKey;
    private String queryString;
    private String scoreOrigin;
    private String division;
    private String key;

    private String pageSize;
    private String pageNum;
    private String bookmark;

    private Channel channel;
    private HFClient client;

    @BeforeEach
    void setUp() throws Exception {
        fabricContruct = new FabricContruct();
        fabricContruct.setUp();

        setData();

        chaincodeInvocation =
                new ChaincodeInvocationImpl();

        this.channel = fabricContruct.getChannel();
        this.client = fabricContruct.getClient();
    }

    //TODO :: ParameterizedTest로 대체...
    private void setData() {
        userTkn = "AB01";
        tradeId = "";
        serviceCode = "";
        sellerTkn = "";
        buyerTkn = "";
        scoreKey = "";
        queryString = "";
        scoreOrigin = "";
        division = "";
        key = "";

        pageSize = "";
        pageNum = "";
        bookmark = "";
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("사용자 추가 테스트")
    @ValueSource(strings = { "AB01" })
    void addUserTest(String userTkn) throws Exception {
        boolean result = chaincodeInvocation.addUser(channel, client, userTkn);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    //    @Disabled
    @DisplayName("사용자 조회 테스트")
    @ValueSource(strings = { "AB01" })
    void queryUserTest(String userTkn) throws Exception {
        UserVo userVo = chaincodeInvocation.queryUser(channel, client, userTkn);
        assertThat(userVo.getUserTkn())
                .isEqualTo("AB01");
    }

    @Test
    @Disabled
    @DisplayName("거래 생성 테스트")
    void createTradeTest() throws Exception {
        boolean result =
                chaincodeInvocation.createTrade(channel, client, tradeId, serviceCode, sellerTkn, buyerTkn);
        assertThat(result).isTrue();
    }

    @Test
    @Disabled
    @DisplayName("")
    void queryTradeWithQueryStringTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeWithQueryString(channel, client, queryString);
        assertThat(tradeVo.getTradeId()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("")
    void queryTradeWithUserTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeWithUser(channel, client, userTkn, ConditionType.ALL,
                OrderType.ASC, ReportType.NORMAL, pageSize, pageNum, bookmark);
        assertThat(tradeVo.getTradeId()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("")
    void queryTradeUserServiceTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeUserService(channel, client, userTkn, serviceCode,
                ConditionType.ALL, OrderType.ASC, ReportType.NORMAL, pageSize, pageNum,
                bookmark);
        assertThat(tradeVo.getTradeId()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("")
    void queryTradeServiceTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeService(channel, client, serviceCode, OrderType.ASC,
                ReportType.NORMAL, pageSize, pageNum, bookmark);
        assertThat(tradeVo.getTradeId()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("거래 조회 테스트")
    void queryTradeWithIdTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryTradeWithId(channel, client, tradeId, serviceCode, sellerTkn,
                buyerTkn);

        assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("점수 조회 테스트")
    void queryScoreTempTest() throws Exception {
        ScoreVo scoreVo = chaincodeInvocation.queryScoreTemp(channel, client, scoreKey);
        assertThat(scoreVo.getExpiryDate()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("검색 조건에 해당하는 임시 평가정수 조회")
    void queryScoreTempWithTradeIdTest() throws Exception {
        TradeVo tradeVo = chaincodeInvocation.queryScoreTempWithTradeId(channel, client, queryString);
        assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
    }

    @Test
    @Disabled
    @DisplayName("거래 완료 처리 테스트")
    void closeTradeTest() throws Exception {
        boolean result = chaincodeInvocation.closeTrade(channel, client, tradeId, userTkn);
        assertThat(result).isTrue();
    }

    @Test
    @Disabled
    @DisplayName("판매자 또는 구매자 점수 등록 테스트")
    void enrollTempScoreTest() throws Exception {
        boolean result =
                chaincodeInvocation.enrollTempScore(channel, client, tradeId, scoreOrigin, division, key);
        assertThat(result).isTrue();
    }

    @Test
    @Disabled
    @DisplayName("거래 ID에 해당하는 점수 조회")
    void queryTempScoreWithConditionTest() throws Exception {
        ScoreVo scoreVo = chaincodeInvocation.queryTempScoreWithCondition(channel, client, tradeId);
        assertThat(scoreVo.getExpiryDate()).isEqualTo("");
    }

    @ParameterizedTest
    @Disabled
    @DisplayName("판매자 구매자 동시에 거래 점수 등록 테스트")
    @ValueSource(strings = { "AB01", "key1234" })
    void enrollScoreTest(String tradeId, String key) throws Exception {
        boolean result = chaincodeInvocation.enrollScore(channel, client, tradeId, key);
        assertThat(result).isTrue();
    }

}
