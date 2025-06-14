package com.ingemark.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    private Long id;
    private String code;
    private String name;
    private BigDecimal priceInEur;
    private BigDecimal priceInUsd;
    private boolean available;

    public Product(Long id, String code, String name, BigDecimal priceInEur, BigDecimal priceInUsd, boolean available) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.priceInEur = priceInEur;
        this.priceInUsd = priceInUsd;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceInEur() {
        return priceInEur.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPriceInEur(BigDecimal priceInEur) {
        this.priceInEur = priceInEur;
    }

    public BigDecimal getPriceInUsd() {
        return priceInUsd;
    }

    public void setPriceInUsd(BigDecimal priceInUsd) {
        this.priceInUsd = priceInUsd;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean isAvailable) {
        this.available = isAvailable;
    }
}
