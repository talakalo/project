package com.automation.selenium;

public enum BrowserType {
    
    CHROME("chrome"),
    FIREFOX("firefox"),
    UNKNOWN("Unknown");
    
    private String name;
    
    BrowserType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
