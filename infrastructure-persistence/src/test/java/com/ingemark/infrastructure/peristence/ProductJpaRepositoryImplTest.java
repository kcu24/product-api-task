package com.ingemark.infrastructure.peristence;

import com.ingemark.domain.model.Product;
import com.ingemark.domain.pagination.PaginatedResult;
import com.ingemark.domain.pagination.PaginationRequest;
import com.ingemark.infranstructure.persistence.entity.ProductEntity;
import com.ingemark.infranstructure.persistence.mapper.ProductMapper;
import com.ingemark.infranstructure.persistence.repository.ProductDataJpaRepository;
import com.ingemark.infranstructure.persistence.repository.impl.ProductJpaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ProductJpaRepositoryImplTest {
    @Mock
    private ProductDataJpaRepository productDataJpaRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductJpaRepositoryImpl repository;

    private final ProductEntity entity = createEntity();
    private final Product domain = createDomain();

    @Test
    void save_shouldMapAndPersist_flowIsCorrectly() {
        //given
        when(productMapper.mapToProductEntity(domain)).thenReturn(entity);
        when(productDataJpaRepository.save(entity)).thenReturn(entity);
        when(productMapper.mapToProductDomain(entity)).thenReturn(domain);

        //when
        Product result = repository.save(domain);

        //then
        assertEquals(domain, result);
        verify(productDataJpaRepository).save(entity);
    }

    @Test
    void findById_shouldReturnMappedDomain_flowIsCorrectly() {
        //given
        when(productDataJpaRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productMapper.mapToProductDomain(entity)).thenReturn(domain);

        //when
        Optional<Product> result = repository.findById(1L);

        //then
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findByCode_shouldReturnMappedDomain_flowIsCorrectly() {
        //given
        when(productDataJpaRepository.findByCode("P001736244")).thenReturn(Optional.of(entity));
        when(productMapper.mapToProductDomain(entity)).thenReturn(domain);

        //when
        Optional<Product> result = repository.findByCode("P001736244");

        //then
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void isCodeAlreadyExists_shouldReturnCorrectFlag() {
        //given
        when(productDataJpaRepository.existsByCode("P001736244")).thenReturn(true);

        //when
        boolean exists = repository.isCodeAlreadyExists("P001736244");

        //then
        assertTrue(exists);
    }

    @Test
    void findAllProducts_shouldReturnPaginatedDomainObjects() {
        //given
        PaginationRequest paginationRequest = new PaginationRequest(0, 10, "code", "ASC");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "code"));

        List<ProductEntity> entityList = List.of(entity);
        Page<ProductEntity> page = new PageImpl<>(entityList, pageable, 1);

        when(productDataJpaRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(productMapper.mapToProductDomain(entity)).thenReturn(domain);

        //when
        PaginatedResult<Product> result = repository.findAllProducts(paginationRequest);

        //then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getItems().size());
        assertEquals(domain, result.getItems().getFirst());
    }

    private ProductEntity createEntity() {
        ProductEntity e = new ProductEntity();
        e.setId(1L);
        e.setCode("P001736244");
        e.setName("Test Product");
        e.setPriceInEur(new BigDecimal("100.00"));
        e.setPriceInUsd(new BigDecimal("110.00"));
        e.setAvailable(true);
        return e;
    }

    private Product createDomain() {
        return new Product(
                1L,
                "P001736244",
                "Test Product",
                new BigDecimal("100.00"),
                new BigDecimal("110.00"),
                true
        );
    }
}
