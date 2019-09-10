package com.github.whitepin.sdk.whitepin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Park
 *
 */
public class UserVo {
	// 데이터 셋의 성격을 구분하는 ID (User는 1)
	@JsonProperty("RecType")
	private int recType;
	
	// 사용자 토큰. 사용자가 누구인지 확인할 때 사용하는 고유한 ID이며 hash값을 문자열로 저장한다. (data-set key)
	@JsonProperty("UserTkn")
	private String userTkn;
	
	// 판매한 거래의 양.
	@JsonProperty("SellAmt")
	private int sellAmt;
	
	// 구매한 거래의 양.
	@JsonProperty("BuyAmt")
	private int buyAmt;
	
	// 판매시, 평가받지 않은 거래의 양. (0점 처리된 판매량)
	@JsonProperty("SellEx")
	private int sellEx;
	
	// 구매시, 평가받지 않은 거래의 양. (0점 처리된 구매량)
	@JsonProperty("BuyEx")
	private int buyEx;
	
	// 사용자 생성날짜
	@JsonProperty("Date")
	private String date;
	
	// 판매 평가점수의 합
	@JsonProperty("SellSum")
	private SellSum sellSum;
	
	// 구매 평가점수의 합
	@JsonProperty("BuySum")
	private BuySum buySum;
	
	// 전체 거래 평가점수의 합
	@JsonProperty("TradeSum")
	private TradeSum tradeSum;
	
	// 판매 평가점수의 평균
	private SellAvg sellAvg;
	
	// 구매 평가점수의 평균
	private BuyAvg buyAvg;
	
	// 전체 거래 평가점수의 평균
	private TradeAvg tradeAvg;
	
	public int getRecType() {
		return recType;
	}

	public String getUserTkn() {
		return userTkn;
	}

	public int getSellAmt() {
		return sellAmt;
	}

	public int getBuyAmt() {
		return buyAmt;
	}

	public int getSellEx() {
		return sellEx;
	}

	public int getBuyEx() {
		return buyEx;
	}

	public String getDate() {
		return date;
	}
	
	public SellSum getSellSum() {
		return sellSum;
	}

	public BuySum getBuySum() {
		return buySum;
	}
	
	public TradeSum getTradeSum() {
		return tradeSum;
	}
	
	public SellAvg getSellAvg() {
		return sellAvg;
	}

	public void setSellAvg(SellAvg sellAvg) {
		this.sellAvg = sellAvg;
	}

	public BuyAvg getBuyAvg() {
		return buyAvg;
	}

	public void setBuyAvg(BuyAvg buyAvg) {
		this.buyAvg = buyAvg;
	}

	public TradeAvg getTradeAvg() {
		return tradeAvg;
	}

	public void setTradeAvg(TradeAvg tradeAvg) {
		this.tradeAvg = tradeAvg;
	}

	public static class SellSum {
		// 판매에 대해 받은 평가점수의 합.
		@JsonProperty("TotSum")
		private int totSum;
		
		// 판매에 대해 1번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum01")
		private int evalSum01;
		
		// 판매에 대해 2번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum02")
		private int evalSum02;
		
		// 판매에 대해 3번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum03")
		private int evalSum03;
		
		public int getTotSum() {
			return totSum;
		}
		
		public int getEvalSum01() {
			return evalSum01;
		}
		
		public int getEvalSum02() {
			return evalSum02;
		}
		
		public int getEvalSum03() {
			return evalSum03;
		}

	}
	
	public static class SellAvg {
		// 판매에 대해 받은 평가점수의 평균.
		private double totAvg;
		
		// 판매에 대해 1번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg1;
		
		// 판매에 대해 2번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg2;
		
		// 판매에 대해 3번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg3;
		
		public double getTotAvg() {
			return totAvg;
		}
		
		public void setTotAvg(double totAvg) {
			this.totAvg = totAvg;
		}
		
		public double getEvalAvg1() {
			return evalAvg1;
		}
		
		public void setEvalAvg1(double evalAvg1) {
			this.evalAvg1 = evalAvg1;
		}
		
		public double getEvalAvg2() {
			return evalAvg2;
		}
		
		public void setEvalAvg2(double evalAvg2) {
			this.evalAvg2 = evalAvg2;
		}
		
		public double getEvalAvg3() {
			return evalAvg3;
		}
		
		public void setEvalAvg3(double evalAvg3) {
			this.evalAvg3 = evalAvg3;
		}
		
	}
	
	public static class BuySum {
		// 구매에 대해 받은 평가점수의 합.
		@JsonProperty("TotSum")
		private int totSum;
		
		// 구매에 대해 1번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum01")
		private int evalSum01;
		
		// 구매에 대해 2번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum02")
		private int evalSum02;
		
		// 구매에 대해 3번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum03")
		private int evalSum03;

		public int getTotSum() {
			return totSum;
		}
		
		public int getEvalSum01() {
			return evalSum01;
		}
		
		public int getEvalSum02() {
			return evalSum02;
		}
		
		public int getEvalSum03() {
			return evalSum03;
		}

	}
	
	public static class BuyAvg {
		// 판매에 대해 받은 평가점수의 평균.
		private double totAvg;
		
		// 판매에 대해 1번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg1;
		
		// 판매에 대해 2번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg2;
		
		// 판매에 대해 3번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg3;
		
		public double getTotAvg() {
			return totAvg;
		}
		
		public void setTotAvg(double totAvg) {
			this.totAvg = totAvg;
		}
		
		public double getEvalAvg1() {
			return evalAvg1;
		}
		
		public void setEvalAvg1(double evalAvg1) {
			this.evalAvg1 = evalAvg1;
		}
		
		public double getEvalAvg2() {
			return evalAvg2;
		}
		
		public void setEvalAvg2(double evalAvg2) {
			this.evalAvg2 = evalAvg2;
		}
		
		public double getEvalAvg3() {
			return evalAvg3;
		}
		
		public void setEvalAvg3(double evalAvg3) {
			this.evalAvg3 = evalAvg3;
		}
		
	}
	
	public static class TradeSum {
		// 전체 거래에 대해 받은 평가점수의 합.
		@JsonProperty("TotSum")
		private int totSum;
		
		// 전체 거래에 대해 1번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum01")
		private int evalSum01;
		
		// 전체 거래에 대해 2번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum02")
		private int evalSum02;
		
		// 전체 거래에 대해 3번 질문에 대하여 받은 평가점수의 합.
		@JsonProperty("EvalSum03")
		private int evalSum03;

		public int getTotSum() {
			return totSum;
		}
		
		public int getEvalSum01() {
			return evalSum01;
		}
		
		public int getEvalSum02() {
			return evalSum02;
		}
		
		public int getEvalSum03() {
			return evalSum03;
		}
	}
	
	public static class TradeAvg {
		// 판매에 대해 받은 평가점수의 평균.
		private double totAvg;
		
		// 판매에 대해 1번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg1;
		
		// 판매에 대해 2번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg2;
		
		// 판매에 대해 3번 질문에 대하여 받은 평가점수의 평균.
		private double evalAvg3;
		
		public double getTotAvg() {
			return totAvg;
		}
		
		public void setTotAvg(double totAvg) {
			this.totAvg = totAvg;
		}
		
		public double getEvalAvg1() {
			return evalAvg1;
		}
		
		public void setEvalAvg1(double evalAvg1) {
			this.evalAvg1 = evalAvg1;
		}
		
		public double getEvalAvg2() {
			return evalAvg2;
		}
		
		public void setEvalAvg2(double evalAvg2) {
			this.evalAvg2 = evalAvg2;
		}
		
		public double getEvalAvg3() {
			return evalAvg3;
		}
		
		public void setEvalAvg3(double evalAvg3) {
			this.evalAvg3 = evalAvg3;
		}
		
	}
}
