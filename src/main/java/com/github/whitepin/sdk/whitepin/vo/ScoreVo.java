package com.github.whitepin.sdk.whitepin.vo;

public class ScoreVo {
	
	// 데이터 셋의 성격을 구분하는 ID (ScoreTemp는 3)
	private int recType;
	
	// 임시 평가점수에 대한 키로 사용되며 무작위 값이다. (data-set key)
	private String scoreKey;
	
	// 거래에 대한 ID이며 hash값을 문자열로 저장한다. (Trade data-set과 동일)
	private String tradeId;
	
	/**
	 * 점수가 실제 거래 데이터에 저장되기 전에 암호화 된 값으로 임시로 저장하고 있는다.
	 * 조건이 달성되면 점수를 실제 거래 데이터에 저장하고 공개한다.
 	 * 조건 : 판매자와 구매자 모두 구매완료한 날로부터 14일 후.
	 * 조건 : 판매자와 구매자가 서로 평가를 완료한 일로부터 5일후
	 */
	private String expiryDate;
	
	// 거래에 대한 상호 평가 점수.
	private Score score;
	
	public int getRecType() {
		return recType;
	}

	public String getScoreKey() {
		return scoreKey;
	}

	public String getTradeId() {
		return tradeId;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public Score getScore() {
		return score;
	}

	public class Score {
		
		// 판매자의 평가 점수. (구매자가 판매자를 평가한 점수) Trade data-set에 들어갈 점수를 보여주기 전, 점수 전문을 양방향 암호화한 문자열 저장. (공개시점 이전 공개 불가능하도록 하기 위해서) 
		private String sellScore;
		
		// 구매자의 평가 점수. (판매자가 구매자를 평가한 점수) Trade data-set에 들어갈 점수를 보여주기 전, 점수 전문을 양방향 암호화한 문자열 저장. (공개시점 이전 공개 불가능하도록 하기 위해서)
		private String buyScore;
		
		public String getSellScore() {
			return sellScore;
		}
		
		public String getBuyScore() {
			return buyScore;
		}
	}
}
