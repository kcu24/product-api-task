package com.ingemark.infranstructure.persistence.mapper;

import com.ingemark.infranstructure.persistence.entity.ProductEntity;
import com.ingemark.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity mapToProductEntity(Product product);

    Product mapToProductDomain(ProductEntity productEntity);
}
