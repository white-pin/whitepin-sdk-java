package com.github.whitepin.sdk.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.whitepin.sdk.contruct.FabricContruct;
import com.github.whitepin.sdk.whitepin.invocation.ChaincodeInvocation;
import com.github.whitepin.sdk.whitepin.invocation.ChaincodeInvocationImpl;
import com.github.whitepin.sdk.whitepin.vo.ScoreVo;
import com.github.whitepin.sdk.whitepin.vo.TradeVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo;

public class FabricChaincodeInvocationIT {
	
	private FabricContruct fabricContruct;
	private ChaincodeInvocation chaincodeInvocation;
	
//	private String userTkn;
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
	
	private void setData() {
		//TODO :: 김용선 대리 작업 완료되면 다시 시작....
//		userTkn = "";
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
	
	@DataPoint("userTkn")
	public static String USER_TKN = "";
	
	@Theory
	@DisplayName("사용자 추가 테스트")
	public void addUserTest(@FromDataPoints("userTkn") String userTkn) throws Exception {
		boolean result = chaincodeInvocation.addUser(userTkn);
		assertThat(result).isTrue();
	}
	
	@Theory
	@DisplayName("사용자 조회 테스트")
	public void queryUserTest(@FromDataPoints("userTkn") String userTkn) throws Exception {
		UserVo userVo = chaincodeInvocation.queryUser(userTkn);
		
		assertThat(userVo.getBuyAmt()).isEqualTo("");
	}
	
	@Test
	@DisplayName("거래 생성 테스트")
	public void createTradeTest() throws Exception {
		boolean result = chaincodeInvocation.createTrade(tradeId, serviceCode, sellerTkn, buyerTkn);
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("거래 조회 테스트")
	public void queryTradeWithIdTest() throws Exception {
		TradeVo tradeVo = chaincodeInvocation.queryTradeWithId(tradeId, serviceCode, sellerTkn, buyerTkn);
		
		assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
	}
	
	@Test
	@DisplayName("점수 조회 테스트")
	public void queryScoreTempTest() throws Exception {
		ScoreVo scoreVo = chaincodeInvocation.queryScoreTemp(scoreKey);
		assertThat(scoreVo.getExpiryDate()).isEqualTo("");
	}
	
	@Test
	@DisplayName("검색 조건에 해당하는 거래 조회")
	public void queryTradeWithConditionTeste() throws Exception {
		TradeVo tradeVo = chaincodeInvocation.queryTradeWithCondition(queryString);
		assertThat(tradeVo.getBuyerTkn()).isEqualTo("");
	}
	
	@Test
	@DisplayName("거래 완료 처리 테스트")
	public void closeTradeTest(@FromDataPoints("userTkn") String userTkn) throws Exception {
		boolean result = chaincodeInvocation.closeTrade(tradeId, userTkn);
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("판매자 또는 구매자 점수 등록 테스트")
	public void enrollTempScoreTest() throws Exception {
		boolean result = chaincodeInvocation.enrollTempScore(tradeId, scoreOrigin, division, key);
		assertThat(result).isTrue();
	}
	
	@Test
	@DisplayName("거래 ID에 해당하는 점수 조회")
	public void queryTempScoreWithConditionTest() throws Exception {
		ScoreVo scoreVo = chaincodeInvocation.queryTempScoreWithCondition(tradeId);
		assertThat(scoreVo.getExpiryDate()).isEqualTo("");
	}
	
	@Test
	@DisplayName("판매자 구매자 동시에 거래 점수 등록 테스트")
	public void enrollScoreTest() throws Exception {
		boolean result = chaincodeInvocation.enrollScore(tradeId, key);
		assertThat(result).isTrue();
	}
	
}
