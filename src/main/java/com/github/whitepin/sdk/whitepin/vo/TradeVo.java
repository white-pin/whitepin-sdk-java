package com.github.whitepin.sdk.whitepin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeVo {

    // 데이터 셋의 성격을 구분하는 ID (Trade는 2)
    @JsonProperty("RecType")
    private int recType;

    // 거래에 대한 ID이며 hash값을 문자열로 저장한다. (data-set key)
    @JsonProperty("TradeId")
    private String tradeId;

    // 서비스 코드는 화이트핀 프로젝트의 하위 서비스 제공자들에 대한 코드이다. 거래를 발생시킨 서비스가 어디인지 규명할때 사용한다.
    @JsonProperty("ServiceCode")
    private String serviceCode;

    // 판매자 토큰. 판매자가 누구인지 확인할 때 사용하는 고유한 ID이며 hash값을 문자열로 저장한다.
    @JsonProperty("SellerTkn")
    private String sellerTkn;

    // 구매자 토큰. 구매자가 누구인지 확인할 때 사용하는 고유한 ID이며 hash값을 문자열로 저장한다.
    @JsonProperty("BuyerTkn")
    private String buyerTkn;

    // 거래가 생성된 시점, 즉 구매자가 구매 요청을 한 시각을 의미한다.
    @JsonProperty("Date")
    private String date;

    //  거래를 확정하는 시점.
    @JsonProperty("Close")
    private Close close;

    // 거래에 대한 평가 점수  
    @JsonProperty("Score")
    private Score score;

    public int getRecType() {
        return recType;
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getSellerTkn() {
        return sellerTkn;
    }

    public String getBuyerTkn() {
        return buyerTkn;
    }

    public String getDate() {
        return date;
    }

    public Close getClose() {
        return close;
    }

    public Score getScore() {
        return score;
    }

    public class Close {
        // 판매자가 거래를 확정했는지 여부 (true : 확정, false : 미확정) 
        @JsonProperty("SellDone")
        private boolean sellDone;

        // 구매자가 거래를 확정했는지 여부 (true : 확정, false : 미확정)
        @JsonProperty("BuyDone")
        private boolean buyDone;

        // 판매자가 거래를 확정한 시각.
        @JsonProperty("SellDate")
        private Date sellDate;

        // 구매자가 거래를 확정한 시각.
        @JsonProperty("BuyDate")
        private Date buyDate;

        public boolean isSellDone() {
            return sellDone;
        }

        public boolean isBuyDone() {
            return buyDone;
        }

        public Date getSellDate() {
            return sellDate;
        }

        public Date getBuyDate() {
            return buyDate;
        }

    }

    public class Score {

        // 판매자의 평가 점수. (판매자가 받은 평가 점수, 구매자가 판매자를 평가한 점수)
        @JsonProperty("SellScore")
        private int[] sellScore;

        // 구매자의 평가 점수. (구매자가 받은 평가 점수, 판매자가 구매자를 평가한 점수)
        @JsonProperty("BuyScore")
        private int[] buyScore;

        public int[] getSellScore() {
            return sellScore;
        }

        public int[] getBuyScore() {
            return buyScore;
        }

    }
}
