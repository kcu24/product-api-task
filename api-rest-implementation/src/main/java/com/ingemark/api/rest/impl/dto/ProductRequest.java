package com.ingemark.api.rest.impl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    @Size(min = 10, max = 10, message = "Code attribute should be 10 characters long")
    private String code;

    @NotBlank(message = "Name attribute should not be empty")
    private String name;

    @Min(value = 0, message = "Price in EUR attribute should be greater than or equal to 0")
    private BigDecimal priceInEur;

    private boolean available;

    public ProductRequest(@JsonProperty("code") String code,
                          @JsonProperty("name") String name,
                          @JsonProperty("priceInEur") BigDecimal priceInEur,
                          @JsonProperty("available") boolean isAvailable) {
        this.code = code;
        this.name = name;
        this.priceInEur = priceInEur;
        this.available = isAvailable;
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
        return priceInEur;
    }

    public void setPriceInEur(BigDecimal priceInEur) {
        this.priceInEur = priceInEur;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", priceInEur=" + priceInEur +
                ", available=" + available +
                '}';
    }
}
