package com.github.whitepin.sdk.whitepin.invocation;

import com.github.whitepin.sdk.whitepin.vo.ScoreVo;
import com.github.whitepin.sdk.whitepin.vo.TradeVo;
import com.github.whitepin.sdk.whitepin.vo.UserVo;

public interface ChaincodeInvocation {
	/**
	 * 사용자 생성 
	 * @param userTkn :: 사용자 토큰
	 * @return
	 */
	public boolean addUser(String userTkn) throws Exception;
	
	/**
	 * 사용자 조회
	 * @param userTkn :: 사용자 토큰
	 */
	public UserVo queryUser(String userTkn) throws Exception;
	
	/**
	 * 거래 생성 
	 * @param tradeId     :: 거래 ID
	 * @param serviceCode :: 서비스 Code ( 000000 : airbnb, 000001 : carrotmarket, .... )
	 * @param sellerTkn   :: 판매자 토큰
	 * @param buyerTkn    :: 구매자 토큰
	 */
	public boolean createTrade(String tradeId, String serviceCode, String sellerTkn, String buyerTkn) throws Exception;
	
	/**
	 * 거래 조회
	 * @param tradeId     :: 거래 ID
	 * @param serviceCode :: 서비스 Code ( 000000 : airbnb, 000001 : carrotmarket, .... )
	 * @param sellerTkn   :: 판매자 토큰
	 * @param buyerTkn    :: 구매자 토큰
	 */
	public TradeVo queryTradeWithId(String tradeId, String serviceCode, String sellerTkn, String buyerTkn) throws Exception;
	
	/**
	 * 임시 평가점수 저장
	 * @param scoreKey    :: 평가 키
	 * @param tradeId     :: 거래 ID
	 */
//	public boolean addScoreMeta(String scoreKey, String tradeId) throws Exception;
	
	/**
	 * 임시 평가정수 조회
	 * @param scoreKey    :: 평가 키
	 */
	public ScoreVo queryScoreTemp(String scoreKey) throws Exception;
	
	/**
	 * 거래 조회
	 * @param queryString :: 거래 조회 조건에 맞는 query string
	 */
	public TradeVo queryTradeWithCondition(String queryString) throws Exception;
	
	/**
	 * 거래 완료 처리 (판매자 또는 구마재)
	 * @param tradeId     :: 거래 ID
	 * @param userTkn     :: 사용자 토큰
	 */
	public boolean closeTrade(String tradeId, String userTkn) throws Exception;
	
	/**
	 * 임시 평가점수 등록 (판매자 또는 구마재)
	 * @param tradeId     :: 거래 ID
	 * @param scoreOrigin :: 평가 점수 e.g.) "[3,4,5]" 의 format
	 * @param division    :: "sell" or "buy"
	 * @param key         :: encryption에 사용될 string
	 */
	public boolean enrollTempScore(String tradeId, String scoreOrigin, String division, String key) throws Exception;
	
	/**
	 * 임시 평가점수 조회 (query string 사용)
	 * @param tradeId     :: 거래 ID
	 */
	public ScoreVo queryTempScoreWithCondition(String tradeId) throws Exception; 
	
	/**
	 * 거래 점수 등록 (판매자, 구매자 동시에)
	 * @param tradeId     :: 거래 ID
	 * @param key         :: 암호화 해제 키
	 */
	public boolean enrollScore(String tradeId, String key) throws Exception;
}
