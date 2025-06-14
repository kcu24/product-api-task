package com.ingemark.api.rest.impl.util;

import com.ingemark.api.rest.impl.dto.ProductResponse;
import com.ingemark.domain.model.Product;

import java.math.BigDecimal;

public final class TestProductDataFactory {

    public static final Long TEST_PRODUCT_ID = 1L;
    public static final String TEST_PRODUCT_CODE = "PROD123456";
    public static final String TEST_INVALID_PRODUCT_CODE = "PROD";
    public static final String TEST_PRODUCT_NAME = "Test Product";
    public static final String TEST_INVALID_PRODUCT_NAME = "";
    public static final BigDecimal TEST_PRODUCT_PRICE_EUR = new BigDecimal("100.01");
    public static final BigDecimal TEST_PRODUCT_PRICE_USD = new BigDecimal("110.01");
    public static final BigDecimal TEST_INVALID_PRODUCT_PRICE_EUR = new BigDecimal("-5");
    public static final boolean TEST_PRODUCT_AVAILABLE = true;
    public static final String NOT_FOUND = "Not Found";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String CONFLICT = "Conflict";
    public static final String INVALID_CODE = "Code attribute should be 10 characters long";
    public static final String INVALID_NAME = "Name attribute should not be empty";
    public static final String INVALID_PRICE_IN_EUR = "Price in EUR attribute should be greater than or equal to 0";

    private TestProductDataFactory() {
    }

    public static Product createDomainProduct() {
        return new Product(
                TEST_PRODUCT_ID,
                TEST_PRODUCT_CODE,
                TEST_PRODUCT_NAME,
                TEST_PRODUCT_PRICE_EUR,
                TEST_PRODUCT_PRICE_USD,
                TEST_PRODUCT_AVAILABLE
        );
    }

    public static ProductResponse createProductResponse() {
        return new ProductResponse(
                TEST_PRODUCT_ID,
                TEST_PRODUCT_CODE,
                TEST_PRODUCT_NAME,
                TEST_PRODUCT_PRICE_EUR,
                TEST_PRODUCT_PRICE_USD,
                TEST_PRODUCT_AVAILABLE
        );
    }

    public static String getRequestMockBody() {
        return """
            {
              "code": "%s",
              "name": "%s",
              "priceInEur": %s,
              "available": %s
            }
            """.formatted(
                TEST_PRODUCT_CODE,
                TEST_PRODUCT_NAME,
                TEST_PRODUCT_PRICE_EUR.toPlainString(),
                TEST_PRODUCT_AVAILABLE
        );
    }

    public static String getRequestMockBodyWithInvalidCode() {
        return """
            {
              "code": "%s",
              "name": "%s",
              "priceInEur": %s,
              "available": %s
            }
            """.formatted(
                TEST_INVALID_PRODUCT_CODE,
                TEST_PRODUCT_NAME,
                TEST_PRODUCT_PRICE_EUR.toPlainString(),
                TEST_PRODUCT_AVAILABLE
        );
    }

    public static String getRequestMockBodyWithEmptyName() {
        return """
            {
              "code": "%s",
              "name": "%s",
              "priceInEur": %s,
              "available": %s
            }
            """.formatted(
                TEST_PRODUCT_CODE,
                TEST_INVALID_PRODUCT_NAME,
                TEST_PRODUCT_PRICE_EUR.toPlainString(),
                TEST_PRODUCT_AVAILABLE
        );
    }

    public static String getRequestMockBodyWithInvalidPriceInEur() {
        return """
            {
              "code": "%s",
              "name": "%s",
              "priceInEur": %s,
              "available": %s
            }
            """.formatted(
                TEST_PRODUCT_CODE,
                TEST_INVALID_PRODUCT_NAME,
                TEST_INVALID_PRODUCT_PRICE_EUR.toPlainString(),
                TEST_PRODUCT_AVAILABLE
        );
    }
}
