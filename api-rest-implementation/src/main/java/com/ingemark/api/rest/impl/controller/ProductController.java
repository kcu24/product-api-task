package com.ingemark.api.rest.impl.controller;

import com.ingemark.application.command.ProductCreateCommand;
import com.ingemark.api.rest.impl.dto.ProductResponse;
import com.ingemark.api.rest.impl.mapper.ProductApiMapper;
import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import com.ingemark.domain.pagination.PaginationRequest;
import com.ingemark.api.rest.impl.dto.ProductRequest;
import com.ingemark.application.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products v1", description = "API for managing products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;
    private final ProductApiMapper productApiMapper;

    public ProductController(ProductService productService, ProductApiMapper productApiMapper) {
        this.productService = productService;
        this.productApiMapper = productApiMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new product",
            description = "Only admins can create products",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product data to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input - bad request"),
                    @ApiResponse(responseCode = "401", description = "UnAuthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "409", description = "Conflict - existing product code"),
                    @ApiResponse(responseCode = "503", description = "Service not available")
            })
    public ResponseEntity<ProductResponse> createProduct(@Validated @RequestBody ProductRequest productRequest) {
        log.info("Received request to create product: {}", productRequest);

        ProductCreateCommand productCreateCommand = productApiMapper.mapToProductCreateCommand(productRequest);
        Product createdDomainProduct = productService.createNewProduct(productCreateCommand);
        
        ProductResponse productResponse = productApiMapper.mapToProductApiResponse(createdDomainProduct);
        URI location = URI.create("/products/" + productResponse.getId());

        return ResponseEntity.created(location).body(productResponse);
    }

    @GetMapping("/by-id/{id}")
    @Operation(summary = "Get product by ID", description = "Fetches a product using its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product response successfully fetched"),
                    @ApiResponse(responseCode = "401", description = "UnAuthorized"),
                    @ApiResponse(responseCode = "404", description = "Product with the given id not found"),
            })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        log.info("Fetching product by ID: {}", id);

        Product fetchedDomainProduct = productService.getProductById(id);
        ProductResponse productApiResponse = productApiMapper.mapToProductApiResponse(fetchedDomainProduct);

        return ResponseEntity.ok(productApiResponse);
    }

    @GetMapping("/by-code/{code}")
    @Operation(summary = "Get product by code", description = "Fetches a product using its unique code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product response successfully fetched"),
                    @ApiResponse(responseCode = "401", description = "UnAuthorized"),
                    @ApiResponse(responseCode = "404", description = "Product with the given code not found"),
            })
    public ResponseEntity<ProductResponse> getProductByCode(
            @Parameter(description = "Product code", required = true)
            @PathVariable String code) {
        log.info("Fetching product by code: {}", code);

        Product fetchedDomainProduct = productService.getProductByCode(code);
        ProductResponse productApiResponse = productApiMapper.mapToProductApiResponse(fetchedDomainProduct);

        return ResponseEntity.ok(productApiResponse);
    }

    @GetMapping
    @Operation(summary = "List all products", description = "Returns a paginated list of products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product response successfully fetched"),
                    @ApiResponse(responseCode = "401", description = "UnAuthorized"),
            })
    public ResponseEntity<PaginatedResult<ProductResponse>> listProducts(
            @Parameter(description = "Page number (starting from 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction: ASC or DESC")
            @RequestParam(defaultValue = "ASC") String sortDir) {

        log.info("Listing all products - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);

        PaginationRequest pagination = new PaginationRequest(page, size, sortBy, sortDir);
        PaginatedResult<Product> fetchedProducts = productService.getAllProducts(pagination);

        List<ProductResponse> productsApiResponse = fetchedProducts.getItems()
                .stream()
                .map(productApiMapper::mapToProductApiResponse)
                .toList();

        log.debug("Fetched {} products", productsApiResponse.size());

        return ResponseEntity.ok(new PaginatedResult<>(productsApiResponse, fetchedProducts.getTotalElements(), fetchedProducts.getTotalPages()));
    }
}