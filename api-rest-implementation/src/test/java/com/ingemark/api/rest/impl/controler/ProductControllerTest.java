package com.ingemark.api.rest.impl.controler;

import com.ingemark.api.rest.impl.controller.ProductController;
import com.ingemark.api.rest.impl.dto.ProductResponse;
import com.ingemark.api.rest.impl.mapper.ProductApiMapper;
import com.ingemark.application.exception.DuplicateProductCodeException;
import com.ingemark.application.exception.ProductNotFoundException;
import com.ingemark.application.repository.ProductRepository;
import com.ingemark.application.service.ProductService;
import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.ingemark.api.rest.impl.util.TestProductDataFactory.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductService service;


    @MockitoBean
    ProductApiMapper productApiMapper;

    @MockitoBean
    private ProductRepository repo;

    @Test
    void createProduct_productIsCreated_returns201Created() throws Exception {
        //given
        String reqBody = getRequestMockBody();

        Product p = createDomainProduct();
        when(service.createNewProduct(any())).thenReturn(p);

        ProductResponse response = createProductResponse();
        when(productApiMapper.mapToProductApiResponse(p)).thenReturn(response);

        //when/then
        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/1"))
                .andExpect(jsonPath("$.id").value(TEST_PRODUCT_ID))
                .andExpect(jsonPath("$.code").value(TEST_PRODUCT_CODE))
                .andExpect(jsonPath("$.name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.priceInEur").value(TEST_PRODUCT_PRICE_EUR))
                .andExpect(jsonPath("$.priceInUsd").value(TEST_PRODUCT_PRICE_USD))
                .andExpect(jsonPath("$.available").value(TEST_PRODUCT_AVAILABLE));

    }

    @Test
    void createProduct_duplicateCodeFound_returns409Conflict() throws Exception {
        //given
        String reqBody = getRequestMockBody();

        Product p = createDomainProduct();
        when(service.createNewProduct(any())).thenThrow(DuplicateProductCodeException.class);

        //when/then
        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.error").value(CONFLICT));
    }

    @ParameterizedTest
    @MethodSource("invalidProductProvider")
    void createProduct_invalidInput_returns400BadRequest() throws Exception {
        //given
        String reqBody = getRequestMockBodyWithInvalidCode();

        //when/then
        mvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(BAD_REQUEST))
                .andExpect(jsonPath("$.message").value(INVALID_CODE));

    }

    @Test
    void getProductById_validProductIdIsAdded_productFoundAndReturned() throws Exception {
        //given
        Product p = createDomainProduct();
        ProductResponse response = createProductResponse();

        when(productApiMapper.mapToProductApiResponse(p)).thenReturn(response);
        when(service.getProductById(TEST_PRODUCT_ID)).thenReturn(p);

        //when/then
        mvc.perform(get("/api/v1/products/by-id/" + TEST_PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_PRODUCT_ID))
                .andExpect(jsonPath("$.code").value(TEST_PRODUCT_CODE))
                .andExpect(jsonPath("$.name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.priceInEur").value(TEST_PRODUCT_PRICE_EUR))
                .andExpect(jsonPath("$.priceInUsd").value(TEST_PRODUCT_PRICE_USD))
                .andExpect(jsonPath("$.available").value(TEST_PRODUCT_AVAILABLE));
    }

    @Test
    void getProductById_givenProductIdNotExists_productIsNotFoundAndNotReturned() throws Exception {
        //given
        Long id = 2L;

        when(service.getProductById(id)).thenThrow(ProductNotFoundException.class);

        //when/then
        mvc.perform(get("/api/v1/products/by-id/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(NOT_FOUND));
    }

    @Test
    void getProductByCode_validProductCodeIsGiven_productIsFound() throws Exception {
        //given
        Product p = createDomainProduct();
        ProductResponse response = createProductResponse();

        when(productApiMapper.mapToProductApiResponse(p)).thenReturn(response);
        when(service.getProductByCode(TEST_PRODUCT_CODE)).thenReturn(p);

        //when/then
        mvc.perform(get("/api/v1/products/by-code/" + TEST_PRODUCT_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_PRODUCT_ID))
                .andExpect(jsonPath("$.code").value(TEST_PRODUCT_CODE))
                .andExpect(jsonPath("$.name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.priceInEur").value(TEST_PRODUCT_PRICE_EUR))
                .andExpect(jsonPath("$.priceInUsd").value(TEST_PRODUCT_PRICE_USD))
                .andExpect(jsonPath("$.available").value(TEST_PRODUCT_AVAILABLE));
    }

    @Test
    void getProductByCode_givenProductCodeNotExists_productIsNotFoundAndNotReturned() throws Exception {
        //given
        String testCode = "1234567891";

        when(service.getProductByCode(testCode)).thenThrow(ProductNotFoundException.class);

        //when/then
        mvc.perform(get("/api/v1/products/by-code/" + testCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(NOT_FOUND));
    }

    @Test
    void listProducts_validRequest_returnsPaginatedProductList() throws Exception {
        // given
        Product product = createDomainProduct();
        ProductResponse response = createProductResponse();

        List<Product> domainProducts = List.of(product);

        PaginatedResult<Product> domainResult = new PaginatedResult<>(domainProducts, 1L, 1);
        when(service.getAllProducts(any())).thenReturn(domainResult);

        when(productApiMapper.mapToProductApiResponse(product)).thenReturn(response);

        // When/Then
        mvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sortBy", "id")
                        .param("sortDir", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.items[0].id").value(TEST_PRODUCT_ID))
                .andExpect(jsonPath("$.items[0].code").value(TEST_PRODUCT_CODE))
                .andExpect(jsonPath("$.items[0].name").value(TEST_PRODUCT_NAME))
                .andExpect(jsonPath("$.items[0].priceInEur").value(TEST_PRODUCT_PRICE_EUR))
                .andExpect(jsonPath("$.items[0].priceInUsd").value(TEST_PRODUCT_PRICE_USD))
                .andExpect(jsonPath("$.items[0].available").value(TEST_PRODUCT_AVAILABLE));
    }

    static Stream<InvalidProductInput> invalidProductProvider() {
        return Stream.of(
                new InvalidProductInput(getRequestMockBodyWithInvalidCode(), INVALID_CODE),
                new InvalidProductInput(getRequestMockBodyWithEmptyName(), INVALID_NAME),
                new InvalidProductInput(getRequestMockBodyWithInvalidPriceInEur(), INVALID_PRICE_IN_EUR));
    }

    private record InvalidProductInput(String json, String expectedMessage) {
    }
}