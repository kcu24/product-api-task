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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ExchangeRateService exchangeRateService;
    private final ProductDtoMapper productMapper;

    public ProductService(ProductRepository productRepository, ExchangeRateService exchangeRateService, ProductDtoMapper productMapper) {
        this.productRepository = productRepository;
        this.exchangeRateService = exchangeRateService;
        this.productMapper = productMapper;
    }

    public Product getProductById(final Long id) {
        logger.debug("Fetching product by ID: {}", id);

        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with given id " + id +  " not found"));
    }

    public Product getProductByCode(final String code) {
        logger.debug("Fetching product by code: {}", code);

        return productRepository
                .findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with the given code '" + code + "' not found"));
    }

    public PaginatedResult<Product> getAllProducts(final PaginationRequest paginationRequest) {
       return productRepository.findAllProducts(paginationRequest);
    }

    @Transactional
    public Product createNewProduct(final ProductCreateCommand productCreateCommand) {
        logger.info("Creating new product with code: {}", productCreateCommand.code());

        boolean productCodeExists = productRepository.isCodeAlreadyExists(productCreateCommand.code());

        if(productCodeExists) {
            logger.warn("Product creation failed: duplicate code '{}'", productCreateCommand.code());
            throw new DuplicateProductCodeException("Product with the code " + productCreateCommand.code() + " already exists");
        }

        Product product = productMapper.mapToProduct(productCreateCommand);
        product.setPriceInUsd(convertEurToUsd(product.getPriceInEur()));

        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());

        return savedProduct;
    }

    private BigDecimal convertEurToUsd(BigDecimal priceInEur) {
        BigDecimal usdExchangeRate = exchangeRateService.getExchangeRate(SupportedCurrency.USD);

        return priceInEur.multiply(usdExchangeRate).setScale(2, RoundingMode.HALF_UP);

    }
}
