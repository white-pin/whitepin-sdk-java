package com.github.whitepin.sdk.whitepin.context;

public enum ConditionType {
    ALL("all")
    ,SELL("sell")
    ,BUY("buy");
    
    private String value;
    
    ConditionType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
