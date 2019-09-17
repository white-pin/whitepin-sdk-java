package com.github.whitepin.sdk.whitepin.invocation;

import java.util.List;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.whitepin.sdk.client.FabricChaincodeClient;
import com.github.whitepin.sdk.whitepin.context.ConditionType;
import com.github.whitepin.sdk.whitepin.context.OrderType;
import com.github.whitepin.sdk.whitepin.context.ReportType;
import com.github.whitepin.sdk.whitepin.util.Calculator;
import com.github.whitepin.sdk.whitepin.vo.ScoreVo;
import com.github.whitepin.sdk.whitepin.vo.TradeVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo.BuyAvg;
import com.github.whitepin.sdk.whitepin.vo.UserVo.BuySum;
import com.github.whitepin.sdk.whitepin.vo.UserVo.SellAvg;
import com.github.whitepin.sdk.whitepin.vo.UserVo.SellSum;
import com.github.whitepin.sdk.whitepin.vo.UserVo.TradeAvg;
import com.github.whitepin.sdk.whitepin.vo.UserVo.TradeSum;

public class ChaincodeInvocationImpl implements ChaincodeInvocation {
    private FabricChaincodeClient fabricChaincodeClient;
    private ChaincodeID chaincodeID;
    private ObjectMapper objectMapper;

    // TODO :: properties로...
    private String CHAINCODE_NAME = "whitepin";
    private Type CHAINCODE_LANG = Type.GO_LANG;

    private String ADD_USER = "addUser";
    private String QUERY_USER = "queryUser";
    private String CREATE_TRADE = "createTrade";
    private String QUERY_TRADE_WITH_QUERY_STRING = "queryTradeWithQueryString";
    private String QUERY_TRADE_WITH_USER = "queryTradeWithUser";
    private String QUERY_TRADE_WITH_USER_SERVICE = "queryTradeWithUserService";
    private String QUERY_TRADE_WITH_SERVICE = "queryTradeWithService";
    private String QUERY_TRADE_WITH_ID = "queryTradeWithId";
    private String QUERY_SCORE_TEMP = "queryScoreTemp";
    private String QUERY_SCORE_TEMP_WITH_TRADE_ID = "queryScoreTempWithTradeId";
    private String CLOSE_TRADE = "closeTrade";
    private String ENROLL_TEMP_SCORE = "enrollTempScore";
    private String ENROLL_SCORE = "enrollScore";
    ////////

    public ChaincodeInvocationImpl() {
        this.chaincodeID = ChaincodeID.newBuilder().setName(CHAINCODE_NAME).build();
        this.objectMapper = new ObjectMapper();
        this.fabricChaincodeClient = new FabricChaincodeClient();
    }

    @Override
    public boolean addUser(Channel channel, HFClient client, String userTkn) throws Exception {
        boolean result = fabricChaincodeClient.invoke(channel, client, ADD_USER, chaincodeID, CHAINCODE_LANG,
                new String[] { userTkn });
        return result;
    }

    @Override
    public UserVo queryUser(Channel channel, HFClient client, String userTkn) throws Exception {
        String result =
                fabricChaincodeClient.query(channel, client, QUERY_USER, chaincodeID, new String[] { userTkn });
        UserVo userVo = objectMapper.readValue(result, UserVo.class);

        /**
         * 판매자 평균 판매 평균점수 = 판매 평가점수 합 / (판매 총 판매량 - 판매 평가받지 않은 판매량)
         */
        SellAvg sellAvg = new SellAvg();
        SellSum sellSum = userVo.getSellSum();
        double sellTotAvg = Calculator.evalTotAvg(sellSum.getTotSum(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setTotAvg(sellTotAvg);

        double sellEvalAvg1 =
                Calculator.evalTotAvg(sellSum.getEvalSum01(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg1(sellEvalAvg1);
        double sellEvalAvg2 =
                Calculator.evalTotAvg(sellSum.getEvalSum02(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg2(sellEvalAvg2);
        double sellEvalAvg3 =
                Calculator.evalTotAvg(sellSum.getEvalSum03(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg3(sellEvalAvg3);
        userVo.setSellAvg(sellAvg);

        /**
         * 구매자 평균 구매 평가점수 합 / (구매 총 구매량 - 구매 평가받지 않은 구매량)
         */
        BuyAvg buyAvg = new BuyAvg();
        BuySum buySum = userVo.getBuySum();
        double buyTotAvg = Calculator.evalTotAvg(buySum.getTotSum(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setTotAvg(buyTotAvg);

        double buyEvalAvg1 =
                Calculator.evalTotAvg(buySum.getEvalSum01(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg1(buyEvalAvg1);
        double buyEvalAvg2 =
                Calculator.evalTotAvg(buySum.getEvalSum02(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg2(buyEvalAvg2);
        double buyEvalAvg3 =
                Calculator.evalTotAvg(buySum.getEvalSum03(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg3(buyEvalAvg3);
        userVo.setBuyAvg(buyAvg);

        /**
         * 거래 평균 거래 평가점수 합 / ((판매 총 판매량 + 구매 총 판매량) - (평가받지 않은 판매량 + 평가받지 않은 구매량))
         */
        TradeAvg tradeAvg = new TradeAvg();
        TradeSum tradeSum = userVo.getTradeSum();
        double tradeTotAvg = Calculator.totAvg(tradeSum.getTotSum(), userVo.getSellAmt(), userVo.getBuyAmt(),
                userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setTotAvg(tradeTotAvg);

        double tradeEvalAvg1 =
                Calculator.totAvg(tradeSum.getEvalSum01(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg1(tradeEvalAvg1);
        double tradeEvalAvg2 =
                Calculator.totAvg(tradeSum.getEvalSum02(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg2(tradeEvalAvg2);
        double tradeEvalAvg3 =
                Calculator.totAvg(tradeSum.getEvalSum03(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg3(tradeEvalAvg3);
        userVo.setTradeAvg(tradeAvg);

        return userVo;
    }

    @Override
    public UserVo queryTotalUser(Channel channel, HFClient client) throws Exception {
        String result =
                fabricChaincodeClient.query(channel, client, QUERY_USER, chaincodeID,
                        new String[] { "TOTAL_USER" });
        UserVo userVo = objectMapper.readValue(result, UserVo.class);

        /**
         * 판매자 평균 판매 평균점수 = 판매 평가점수 합 / (판매 총 판매량 - 판매 평가받지 않은 판매량)
         */
        SellAvg sellAvg = new SellAvg();
        SellSum sellSum = userVo.getSellSum();
        double sellTotAvg = Calculator.evalTotAvg(sellSum.getTotSum(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setTotAvg(sellTotAvg);

        double sellEvalAvg1 =
                Calculator.evalTotAvg(sellSum.getEvalSum01(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg1(sellEvalAvg1);
        double sellEvalAvg2 =
                Calculator.evalTotAvg(sellSum.getEvalSum02(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg2(sellEvalAvg2);
        double sellEvalAvg3 =
                Calculator.evalTotAvg(sellSum.getEvalSum03(), userVo.getSellAmt(), userVo.getSellEx());
        sellAvg.setEvalAvg3(sellEvalAvg3);
        userVo.setSellAvg(sellAvg);

        /**
         * 구매자 평균 구매 평가점수 합 / (구매 총 구매량 - 구매 평가받지 않은 구매량)
         */
        BuyAvg buyAvg = new BuyAvg();
        BuySum buySum = userVo.getBuySum();
        double buyTotAvg = Calculator.evalTotAvg(buySum.getTotSum(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setTotAvg(buyTotAvg);

        double buyEvalAvg1 =
                Calculator.evalTotAvg(buySum.getEvalSum01(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg1(buyEvalAvg1);
        double buyEvalAvg2 =
                Calculator.evalTotAvg(buySum.getEvalSum02(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg2(buyEvalAvg2);
        double buyEvalAvg3 =
                Calculator.evalTotAvg(buySum.getEvalSum03(), userVo.getBuyAmt(), userVo.getBuyEx());
        buyAvg.setEvalAvg3(buyEvalAvg3);
        userVo.setBuyAvg(buyAvg);

        /**
         * 거래 평균 거래 평가점수 합 / ((판매 총 판매량 + 구매 총 판매량) - (평가받지 않은 판매량 + 평가받지 않은 구매량))
         */
        TradeAvg tradeAvg = new TradeAvg();
        TradeSum tradeSum = userVo.getTradeSum();
        double tradeTotAvg = Calculator.totAvg(tradeSum.getTotSum(), userVo.getSellAmt(), userVo.getBuyAmt(),
                userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setTotAvg(tradeTotAvg);

        double tradeEvalAvg1 =
                Calculator.totAvg(tradeSum.getEvalSum01(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg1(tradeEvalAvg1);
        double tradeEvalAvg2 =
                Calculator.totAvg(tradeSum.getEvalSum02(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg2(tradeEvalAvg2);
        double tradeEvalAvg3 =
                Calculator.totAvg(tradeSum.getEvalSum03(), userVo.getSellAmt(), userVo.getBuyAmt(),
                        userVo.getSellEx(), userVo.getBuyEx());
        buyAvg.setEvalAvg3(tradeEvalAvg3);
        userVo.setTradeAvg(tradeAvg);

        return userVo;
    }

    @Override
    public boolean createTrade(Channel channel, HFClient client, String tradeId, String serviceCode,
            String sellerTkn,
            String buyerTkn) throws Exception {
        boolean result =
                fabricChaincodeClient.invoke(channel, client, CREATE_TRADE, chaincodeID, CHAINCODE_LANG,
                        new String[] { tradeId, serviceCode, sellerTkn, buyerTkn });
        return result;
    }

    @Override
    public List<TradeVo> queryTradeWithQueryString(Channel channel, HFClient client, String queryString)
            throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_QUERY_STRING, chaincodeID,
                new String[] { queryString });

        List<TradeVo> tradeVos = objectMapper.readValue(result, new TypeReference<List<TradeVo>>() {});
        return tradeVos;
    }

    @Override
    public List<TradeVo> queryTradeWithUser(Channel channel, HFClient client, String userTkn,
            ConditionType conditionType,
            OrderType orderType, ReportType reportType, String pageSize, String pageNum, String bookmark)
            throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_USER, chaincodeID,
                new String[] { userTkn, conditionType.getValue(), orderType.getValue(), reportType.getValue(),
                        pageSize, pageNum, bookmark });
        List<TradeVo> tradeVos = objectMapper.readValue(result, new TypeReference<List<TradeVo>>() {});
        return tradeVos;
    }

    @Override
    public List<TradeVo> queryTradeWithUser(Channel channel, HFClient client, String userTkn,
            ConditionType conditionType,
            OrderType orderType, String pageSize, String pageNum, String bookmark)
            throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_USER, chaincodeID,
                new String[] { userTkn, conditionType.getValue(), orderType.getValue(),
                        ReportType.PAGE.getValue(), pageSize, pageNum, bookmark });
        List<TradeVo> tradeVos = objectMapper.readValue(result, new TypeReference<List<TradeVo>>() {});
        return tradeVos;
    }

    @Override
    public List<TradeVo> queryTradeWithUserService(Channel channel, HFClient client, String userTkn,
            String serviceCode,
            ConditionType conditionType,
            OrderType orderType, ReportType reportType, String pageSize, String pageNum,
            String bookmark) throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_USER_SERVICE, chaincodeID,
                new String[] { userTkn, serviceCode, conditionType.getValue(), orderType.getValue(),
                        reportType.getValue(), pageSize, pageNum, bookmark });
        List<TradeVo> tradeVos = objectMapper.readValue(result, new TypeReference<List<TradeVo>>() {});
        return tradeVos;
    }

    @Override
    public List<TradeVo> queryTradeWithService(Channel channel, HFClient client, String serviceCode,
            OrderType orderType,
            ReportType reportType, String pageSize, String pageNum, String bookmark) throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_SERVICE, chaincodeID,
                new String[] { serviceCode, orderType.getValue(), reportType.getValue(), pageSize, pageNum,
                        bookmark });
        List<TradeVo> tradeVos = objectMapper.readValue(result, new TypeReference<List<TradeVo>>() {});
        return tradeVos;
    }

    @Override
    public TradeVo queryTradeWithId(Channel channel, HFClient client, String tradeId) throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_TRADE_WITH_ID, chaincodeID,
                new String[] { tradeId });
        TradeVo tradeVo = objectMapper.readValue(result, TradeVo.class);
        return tradeVo;
    }

    @Override
    public ScoreVo queryScoreTemp(Channel channel, HFClient client, String scoreKey) throws Exception {
        String result = fabricChaincodeClient.query(channel, client, QUERY_SCORE_TEMP, chaincodeID,
                new String[] { scoreKey });
        ScoreVo scoreVo = objectMapper.readValue(result, ScoreVo.class);
        return scoreVo;
    }

    @Override
    public ScoreVo queryScoreTempWithTradeId(Channel channel, HFClient client, String tradeId)
            throws Exception {
        String result =
                fabricChaincodeClient.query(channel, client, QUERY_SCORE_TEMP_WITH_TRADE_ID, chaincodeID,
                        new String[] { tradeId });
        ScoreVo scoreVo = objectMapper.readValue(result, ScoreVo.class);
        return scoreVo;
    }

    @Override
    public boolean closeTrade(Channel channel, HFClient client, String tradeId, String userTkn)
            throws Exception {
        boolean result = fabricChaincodeClient.invoke(channel, client, CLOSE_TRADE, chaincodeID, CHAINCODE_LANG,
                new String[] { tradeId, userTkn });
        return result;
    }

    @Override
    public boolean enrollTempScore(Channel channel, HFClient client, String tradeId, String userTkn,
            String scoreOrigin, String key) throws Exception {
        boolean result =
                fabricChaincodeClient.invoke(channel, client, ENROLL_TEMP_SCORE, chaincodeID, CHAINCODE_LANG,
                        new String[] { tradeId, userTkn, scoreOrigin, key });
        return result;
    }

    @Override
    public boolean enrollTempScore(Channel channel, HFClient client, String tradeId, String scoreOrigin,
            String userTkn) throws Exception {
        boolean result =
                fabricChaincodeClient.invoke(channel, client, ENROLL_TEMP_SCORE, chaincodeID, CHAINCODE_LANG,
                        new String[] { tradeId, scoreOrigin, userTkn, "key1234" });
        return result;
    }

    @Override
    public boolean enrollScore(Channel channel, HFClient client, String tradeId, String key) throws Exception {
        boolean result =
                fabricChaincodeClient.invoke(channel, client, ENROLL_SCORE, chaincodeID, CHAINCODE_LANG,
                        new String[] { tradeId, key });
        return result;
    }

}
