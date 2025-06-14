package com.ingemark.infranstructure.persistence.repository;

import com.ingemark.infranstructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDataJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByCode(String code);

    boolean existsByCode(String code);
}
