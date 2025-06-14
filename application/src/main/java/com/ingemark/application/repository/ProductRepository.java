package com.ingemark.application.repository;

import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import com.ingemark.domain.pagination.PaginationRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findByCode(String code);

    PaginatedResult<Product> findAllProducts(PaginationRequest paginationRequest);

    boolean isCodeAlreadyExists(String code);
}
