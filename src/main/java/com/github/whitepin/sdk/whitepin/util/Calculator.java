package com.github.whitepin.sdk.whitepin.util;

public class Calculator {
	
	/**
	 * 판매(구매) 평균 점수
	 * @param sum  :: 판매(구매) 평가 점수 합
	 * @param amt  :: 판매(구매) 총 판매량
	 * @param ex   :: 판매(구매) 평가받지 않은 판매량
	 * @return
	 */
	public static double evalTotAvg(int sum, int amt, int ex) {
		double result = 0.0D;
		if(amt - ex != 0) 
			result = sum / ( amt - ex );
		return result;
	}
	
	/**
	 * total 평균 
	 * @param tradeTotSum  :: 거래 total 합
	 * @param sellAmt      :: 판매 총 판매량
	 * @param buyAmt       :: 구매 총 판매량
	 * @param sellEx       :: 판매 평가받지 않은 판매량
	 * @param buyEx        :: 구매 평가받지 않은 판매량
	 * @return
	 */
	public static double totAvg(int tradeTotSum, int sellAmt, int buyAmt, int sellEx, int buyEx) {
		double result = 0.0D;
		if(((sellAmt + buyAmt) - (sellEx + buyEx)) != 0)
			result = tradeTotSum / ((sellAmt + buyAmt) - (sellEx + buyEx));
		return result;
	}
}
