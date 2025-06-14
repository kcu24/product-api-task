package com.ingemark.application.request;

import java.util.Currency;

public enum SupportedCurrency {
    EUR("Euro", "EUR"),
    USD("US Dollar", "USD"),
    GBP("British Pound", "GBP");

    private final String displayName;
    private final String code;

    SupportedCurrency(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public Currency toJavaCurrency() {
        return Currency.getInstance(code);
    }
}
