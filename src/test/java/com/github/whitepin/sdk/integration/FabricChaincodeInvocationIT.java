package com.github.whitepin.sdk.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.junit.Before;
import org.junit.Test;

import com.github.whitepin.sdk.contruct.FabricContruct;
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
	
	@Before
	public void setUp() throws Exception {
		fabricContruct = new FabricContruct();
		fabricContruct.setUp();
		
		setData();
		
		chaincodeInvocation = new ChaincodeInvocationImpl(fabricContruct.getChannel(), fabricContruct.getClient());
	}
	
	@Test
	public void addUserTest() throws Exception {
		boolean result = chaincodeInvocation.addUser(userTkn);
		assertThat(result).isTrue();
	}
	
	@Test
	public void queryUserTest() throws Exception {
		UserVo userVo = chaincodeInvocation.queryUser(userTkn);
		
		assertThat(userVo.getBuyAmt()).isEqualTo("");
	}
	
	@Test
	public void createTradeTest() throws Exception {
		boolean result = chaincodeInvocation.createTrade(tradeId, serviceCode, sellerTkn, buyerTkn);
		assertThat(result).isTrue();
	}
	
	@Test
	public void queryTradeWithIdTest() throws Exception {
		TradeVo tradeVo = chaincodeInvocation.queryTradeWithId(tradeId, serviceCode, sellerTkn, buyerTkn);
		
		assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
	}
	
	@Test
	public void queryScoreTempTest() throws Exception {
		ScoreVo scoreVo = chaincodeInvocation.queryScoreTemp(scoreKey);
		assertThat(scoreVo.getExpiryDate()).isEqualTo("");
	}
	
	@Test
	public void queryTradeWithConditionTeste() throws Exception {
		TradeVo tradeVo = chaincodeInvocation.queryTradeWithCondition(queryString);
		assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
	}
	
	@Test
	public void closeTradeTest() throws Exception {
		boolean result = chaincodeInvocation.closeTrade(tradeId, userTkn);
		assertThat(result).isTrue();
	}
	
	@Test
	public void enrollTempScoreTest() throws Exception {
		boolean result = chaincodeInvocation.enrollTempScore(tradeId, scoreOrigin, division, key);
		assertThat(result).isTrue();
	}
	
	@Test
	public void queryTempScoreWithConditionTest() throws Exception {
		ScoreVo scoreVo = chaincodeInvocation.queryTempScoreWithCondition(tradeId);
		assertThat(scoreVo.getExpiryDate()).isEqualTo("");
	}
	
	@Test
	public void enrollScoreTest() throws Exception {
		boolean result = chaincodeInvocation.enrollScore(tradeId, key);
		assertThat(result).isTrue();
	}
	
	private void setData() {
		userTkn = "";
		tradeId = "";
		serviceCode = "";
		sellerTkn = "";
		buyerTkn = "";
		scoreKey = "";
		queryString = "";
		scoreOrigin = "";
		division = "";
		key = "";
	}
}
