package com.ingemark.application.service;

import com.ingemark.application.command.ProductCreateCommand;
import com.ingemark.application.exception.DuplicateProductCodeException;
import com.ingemark.application.exception.ProductNotFoundException;
import com.ingemark.application.exchange.ExchangeRateService;
import com.ingemark.application.exchange.mapper.ProductDtoMapper;
import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import com.ingemark.domain.pagination.PaginationRequest;
import com.ingemark.application.repository.ProductRepository;
import com.ingemark.application.request.SupportedCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Spy
    private ProductDtoMapper mapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewProduct_inputDataIsValid_productIsCreatedSuccessfully() {
        //given
        var productCreateCommand = createTestProductCommand();
        when(repository.isCodeAlreadyExists(productCreateCommand.code())).thenReturn(false);
        when(exchangeRateService.getExchangeRate(SupportedCurrency.USD)).thenReturn(BigDecimal.valueOf(1.1));

        Product domainProduct = createDomainProduct(productCreateCommand);
        when(mapper.mapToProduct(productCreateCommand)).thenReturn(domainProduct);

        Product savedProduct = new Product(1L, productCreateCommand.code(), productCreateCommand.name(), productCreateCommand.priceInEur(), BigDecimal.valueOf(11.00), productCreateCommand.available());
        when(repository.save(any())).thenReturn(savedProduct);

        //when
        Product result = service.createNewProduct(productCreateCommand);

        //then
        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.valueOf(11.00), result.getPriceInUsd());
    }

    @Test
    void createNewProduct_codeThatAlreadyExistIsUsed_DuplicateProductCodeExceptionIsThrown() {
        //given
        var productCreateCommand = createTestProductCommand();
        when(repository.isCodeAlreadyExists(productCreateCommand.code())).thenReturn(true);

        //when/then
        assertThrows(DuplicateProductCodeException.class, () -> service.createNewProduct(productCreateCommand));
    }

    @Test
    void getProductByCode_productWithCodeExists_productIsReturned() {
        // given
        Product product = createFullDomainProduct();

        when(repository.findByCode("CODE123456")).thenReturn(Optional.of(product));

        // when
        Product result = service.getProductByCode("CODE123456");

        // then
        assertNotNull(result);
        assertEquals(42L, result.getId());
        assertEquals("CODE123456", result.getCode());
        assertEquals("Test Product", result.getName());
        assertEquals(BigDecimal.valueOf(15.22), result.getPriceInEur());
        assertEquals(BigDecimal.valueOf(17.22), result.getPriceInUsd());
        assertTrue(result.isAvailable());
    }

    @Test
    void getProductById_productWithIdExists_productIsReturned() {
        // given
        Product product = createFullDomainProduct();

        when(repository.findById(42L)).thenReturn(Optional.of(product));

        // when
        Product result = service.getProductById(42L);

        // then
        assertNotNull(result);
        assertEquals(42L, result.getId());
        assertEquals("CODE123456", result.getCode());
        assertEquals("Test Product", result.getName());
        assertEquals(BigDecimal.valueOf(15.22), result.getPriceInEur());
        assertEquals(BigDecimal.valueOf(17.22), result.getPriceInUsd());
        assertTrue(result.isAvailable());
    }

    @Test
    void getProductById_productWithIdDoNotExists_ProductNotFoundExceptionExceptionIsThrown() {
        //given
        when(repository.findById(42L)).thenReturn(Optional.empty());

        //when/then
        assertThrows(ProductNotFoundException.class, () -> service.getProductById(42L));
    }

    @Test
    void getAllProducts() {
        //given
        PaginationRequest pr = new PaginationRequest(1, 5, "code", "ASC");
        PaginatedResult<Product> pg = new PaginatedResult<>(List.of(), 50, 10);
        when(repository.findAllProducts(pr)).thenReturn(pg);

        //when/then
        assertSame(pg, service.getAllProducts(pr));
    }

    private ProductCreateCommand createTestProductCommand() {
        return new ProductCreateCommand("CODE123456", "Test", BigDecimal.TEN, true);
    }

    private Product createDomainProduct(ProductCreateCommand cmd) {
        return new Product(
                null,
                cmd.code(),
                cmd.name(),
                cmd.priceInEur(),
                null,
                cmd.available()
        );
    }

    private Product createFullDomainProduct() {
        return new Product(
                42L,
                "CODE123456",
                "Test Product",
                BigDecimal.valueOf(15.22),
                BigDecimal.valueOf(17.22),
                true
        );
    }
}