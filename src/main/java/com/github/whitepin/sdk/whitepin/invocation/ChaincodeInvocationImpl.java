package com.github.whitepin.sdk.whitepin.invocation;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.whitepin.sdk.client.FabricChaincodeClient;
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
	private String QUERY_TRADE_WITH_ID = "queryTradeWithId";
//	private String ADD_SCORE_META = "addScoreMeta";
	private String QUERY_SCORE_TEMP = "queryScoreTemp";
	private String QUERY_TRADE_WITH_CONDITION = "queryTradeWithCondition";
	private String CLOSE_TRADE = "closeTrade";
	private String ENROLL_TEMP_SCORE = "enrollTempScore";
	private String QUERY_TEMP_SCORE_WITH_CONDITION = "queryTempScoreWithCondition";
	private String ENROLL_SCORE = "enrollScore";
	////////
	
	public ChaincodeInvocationImpl(Channel channel, HFClient client) {
		this.fabricChaincodeClient = new FabricChaincodeClient(channel, client);
		this.chaincodeID = ChaincodeID.newBuilder().setName(CHAINCODE_NAME).build();
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public boolean addUser(String userTkn) throws Exception {
		boolean result = fabricChaincodeClient.invoke(ADD_USER, chaincodeID, CHAINCODE_LANG, new String[] {userTkn});
		return result;
	}

	@Override
	public UserVo queryUser(String userTkn) throws Exception {
		String result = fabricChaincodeClient.query(QUERY_USER, chaincodeID, new String[] {userTkn});
		UserVo userVo = objectMapper.readValue(result, UserVo.class);
		
		/**
		 * 판매자 평균
		 * 판매 평균점수 = 판매 평가점수 합 / (판매 총 판매량 - 판매 평가받지 않은 판매량)
		 */
		SellAvg sellAvg = new SellAvg();
		SellSum sellSum = userVo.getSellSum();
		double sellTotAvg = Calculator.evalTotAvg(sellSum.getTotSum(), userVo.getSellAmt(), userVo.getSellEx());
		sellAvg.setTotAvg(sellTotAvg);
		
		double sellEvalAvg1 = Calculator.evalTotAvg(sellSum.getEvalSum01(), userVo.getSellAmt(), userVo.getSellEx());
		sellAvg.setEvalAvg1(sellEvalAvg1);
		double sellEvalAvg2 = Calculator.evalTotAvg(sellSum.getEvalSum02(), userVo.getSellAmt(), userVo.getSellEx());
		sellAvg.setEvalAvg1(sellEvalAvg2);
		double sellEvalAvg3 = Calculator.evalTotAvg(sellSum.getEvalSum03(), userVo.getSellAmt(), userVo.getSellEx());
		sellAvg.setEvalAvg1(sellEvalAvg3);
		userVo.setSellAvg(sellAvg);
		
		/**
		 * 구매자 평균
		 * 구매 평가점수 합 / (구매 총 구매량 - 구매 평가받지 않은 구매량)
		 */
		BuyAvg buyAvg = new BuyAvg();
		BuySum buySum = userVo.getBuySum();
		double buyTotAvg = Calculator.evalTotAvg(buySum.getTotSum(), userVo.getBuyAmt(), userVo.getBuyEx());
		buyAvg.setTotAvg(buyTotAvg);
		
		double buyEvalAvg1 = Calculator.evalTotAvg(buySum.getEvalSum01(), userVo.getBuyAmt(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(buyEvalAvg1);
		double buyEvalAvg2 = Calculator.evalTotAvg(buySum.getEvalSum02(), userVo.getBuyAmt(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(buyEvalAvg2);
		double buyEvalAvg3 = Calculator.evalTotAvg(buySum.getEvalSum03(), userVo.getBuyAmt(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(buyEvalAvg3);
		userVo.setBuyAvg(buyAvg);
		
		/**
		 * 거래 평균
		 * 거래 평가점수 합 / ((판매 총 판매량 + 구매 총 판매량) - (평가받지 않은 판매량 + 평가받지 않은 구매량))
		 */
		TradeAvg tradeAvg = new TradeAvg();
		TradeSum tradeSum = userVo.getTradeSum();
		double tradeTotAvg = Calculator.totAvg(tradeSum.getTotSum(), userVo.getSellAmt(), userVo.getBuyAmt(), userVo.getSellEx(), userVo.getBuyEx());
		buyAvg.setTotAvg(tradeTotAvg);
		
		double tradeEvalAvg1 = Calculator.totAvg(tradeSum.getEvalSum01(), userVo.getSellAmt(), userVo.getBuyAmt(), userVo.getSellEx(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(tradeEvalAvg1);
		double tradeEvalAvg2 = Calculator.totAvg(tradeSum.getEvalSum02(), userVo.getSellAmt(), userVo.getBuyAmt(), userVo.getSellEx(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(tradeEvalAvg2);
		double tradeEvalAvg3 = Calculator.totAvg(tradeSum.getEvalSum03(), userVo.getSellAmt(), userVo.getBuyAmt(), userVo.getSellEx(), userVo.getBuyEx());
		buyAvg.setEvalAvg1(tradeEvalAvg3);
		userVo.setTradeAvg(tradeAvg);
		
		return userVo;
	}

	@Override
	public boolean createTrade(String tradeId, String serviceCode, String sellerTkn, String buyerTkn) throws Exception {
		boolean result = fabricChaincodeClient.invoke(CREATE_TRADE, chaincodeID, CHAINCODE_LANG, new String[] {tradeId, serviceCode, sellerTkn, buyerTkn});
		return result;
	}

	@Override
	public TradeVo queryTradeWithId(String tradeId, String serviceCode, String sellerTkn, String buyerTkn) throws Exception {
		String result = fabricChaincodeClient.query(QUERY_TRADE_WITH_ID, chaincodeID, new String[] {tradeId, serviceCode, sellerTkn, buyerTkn});
		TradeVo tradeVo = objectMapper.readValue(result, TradeVo.class);
		return tradeVo;
	}

//	@Override
//	public boolean addScoreMeta(String scoreKey, String tradeId) throws Exception {
//		boolean result = fabricChaincodeClient.invoke(ADD_SCORE_META, chaincodeID, CHAINCODE_LANG, new String[] {scoreKey, tradeId});
//		return result;
//	}

	@Override
	public ScoreVo queryScoreTemp(String scoreKey) throws Exception {
		String result = fabricChaincodeClient.query(QUERY_SCORE_TEMP, chaincodeID, new String[] {scoreKey});
		ScoreVo scoreVo = objectMapper.readValue(result, ScoreVo.class);
		return scoreVo;
	}

	@Override
	public TradeVo queryTradeWithCondition(String queryString) throws Exception {
		String result = fabricChaincodeClient.query(QUERY_TRADE_WITH_CONDITION, chaincodeID, new String[] {queryString});
		TradeVo tradeVo = objectMapper.readValue(result, TradeVo.class);
		return tradeVo;
	}

	@Override
	public boolean closeTrade(String tradeId, String userTkn) throws Exception {
		boolean result = fabricChaincodeClient.invoke(CLOSE_TRADE, chaincodeID, CHAINCODE_LANG, new String[] {tradeId, userTkn});
		return result;
	}

	@Override
	public boolean enrollTempScore(String tradeId, String scoreOrigin, String division, String key) throws Exception {
		boolean result = fabricChaincodeClient.invoke(ENROLL_TEMP_SCORE, chaincodeID, CHAINCODE_LANG, new String[] {tradeId, scoreOrigin, division, key});
		return result;
	}

	@Override
	public ScoreVo queryTempScoreWithCondition(String tradeId) throws Exception {
		String result = fabricChaincodeClient.query(QUERY_TEMP_SCORE_WITH_CONDITION, chaincodeID, new String[] {tradeId});
		ScoreVo scoreVo = objectMapper.readValue(result, ScoreVo.class);
		return scoreVo;
	}

	@Override
	public boolean enrollScore(String tradeId, String key) throws Exception {
		boolean result = fabricChaincodeClient.invoke(ENROLL_SCORE, chaincodeID, CHAINCODE_LANG, new String[] {tradeId, key});
		return result;
	}

}
