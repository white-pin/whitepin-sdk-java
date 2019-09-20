package com.github.whitepin.sdk.whitepin.util;

import java.text.DecimalFormat;

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
        if (amt - ex != 0)
            result = (double) sum / (amt - ex);
        DecimalFormat stringFormat = new DecimalFormat(".#");
        return Double.valueOf(stringFormat.format(result));
    }
    
    /**
     * 판매(구매) 평균 점수
     * @param sum  :: 판매(구매) 평가 점수 합
     * @param amt  :: 판매(구매) 총 판매량
     * @param ex   :: 판매(구매) 평가받지 않은 판매량
     * @param count:: 판매 항목 개수
     * @return
     */
    public static double evalTotAvg(int sum, int amt, int ex, int count) {
        double result = 0.0D;
        if (amt - ex != 0)
            result = (double) sum / (amt - ex) / count;
        DecimalFormat stringFormat = new DecimalFormat(".#");
        return Double.valueOf(stringFormat.format(result));
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
        if (((sellAmt + buyAmt) - (sellEx + buyEx)) != 0)
            result = (double) tradeTotSum / ((sellAmt + buyAmt) - (sellEx + buyEx));
        DecimalFormat stringFormat = new DecimalFormat(".#");
        return Double.valueOf(stringFormat.format(result));
    }
    
    /**
     * total 평균
     * @param tradeTotSum  :: 거래 total 합
     * @param sellAmt      :: 판매 총 판매량
     * @param buyAmt       :: 구매 총 판매량
     * @param sellEx       :: 판매 평가받지 않은 판매량
     * @param buyEx        :: 구매 평가받지 않은 판매량
     * @param count        :: 평가 항목 개수
     * @return
     */
    public static double totAvg(int tradeTotSum, int sellAmt, int buyAmt, int sellEx, int buyEx, int count) {
        double result = 0.0D;
        if (((sellAmt + buyAmt) - (sellEx + buyEx)) != 0)
            result = (double) tradeTotSum / ((sellAmt + buyAmt) - (sellEx + buyEx)) / count;
        DecimalFormat stringFormat = new DecimalFormat(".#");
        return Double.valueOf(stringFormat.format(result));
    }
}
