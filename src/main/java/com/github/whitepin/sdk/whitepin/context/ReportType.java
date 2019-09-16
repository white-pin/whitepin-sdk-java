package com.github.whitepin.sdk.whitepin.context;

public enum ReportType {
    NORMAL("normal")
    ,PAGE("page");
    
    private String value;
    
    ReportType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
