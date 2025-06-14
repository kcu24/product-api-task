package com.ingemark.infranstructure.persistence.repository.impl;

import com.ingemark.infranstructure.persistence.entity.ProductEntity;
import com.ingemark.infranstructure.persistence.mapper.ProductMapper;
import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import com.ingemark.domain.pagination.PaginationRequest;
import com.ingemark.application.repository.ProductRepository;
import com.ingemark.infranstructure.persistence.repository.ProductDataJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductJpaRepositoryImpl implements ProductRepository {

    private final ProductDataJpaRepository jpaRepository;
    private final ProductMapper productMapper;

    public ProductJpaRepositoryImpl(ProductDataJpaRepository jpaRepository, ProductMapper productMapper) {
        this.jpaRepository = jpaRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = productMapper.mapToProductEntity(product);
        ProductEntity savedProductEntity = jpaRepository.save(productEntity);

        return productMapper.mapToProductDomain(savedProductEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<ProductEntity> productEntity = jpaRepository.findById(id);

        return productEntity.map(productMapper::mapToProductDomain);

    }

    @Override
    public Optional<Product> findByCode(String code) {
        Optional<ProductEntity> productEntity = jpaRepository.findByCode(code);

        return productEntity.map(productMapper::mapToProductDomain);
    }


    @Override
    public PaginatedResult<Product> findAllProducts(PaginationRequest paginationRequest) {
        Page<ProductEntity> productEntities = jpaRepository.findAll(buildPageableObject(paginationRequest));
        List<Product> products = productEntities.map(productMapper::mapToProductDomain).getContent();

        return new PaginatedResult<>(products, productEntities.getTotalElements(), productEntities.getTotalPages());
    }

    @Override
    public boolean isCodeAlreadyExists(String code) {
        return jpaRepository.existsByCode(code);
    }

    private Pageable buildPageableObject(PaginationRequest paginationRequest) {
        return PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection()), paginationRequest.getSortBy()));
    }
}
