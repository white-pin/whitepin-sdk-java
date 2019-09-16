package com.github.whitepin.sdk.whitepin.context;

public enum OrderType {
    DESC("desc")
    ,ASC("asc");
    
    private String value;
    
    OrderType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
