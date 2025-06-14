package com.ingemark.infrastructure.exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRateDto {
    @JsonProperty("kupovni_tecaj")
    private String buyingRate;

    @JsonProperty("prodajni_tecaj")
    private String sellingRate;

    @JsonProperty("srednji_tecaj")
    private String midRate;

    public String getMidRate() {
        return midRate;
    }

    public String getBuyingRate() {
        return buyingRate;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public void setMidRate(String midRate) {
        this.midRate = midRate;
    }

    public BigDecimal getMidRateAsBigDecimal() {
        return new BigDecimal(midRate.replace(",", "."))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
