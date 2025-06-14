package com.ingemark.application.exchange.mapper;

import com.ingemark.application.command.ProductCreateCommand;
import com.ingemark.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {

    @Mapping(target = "priceInUsd", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product mapToProduct(ProductCreateCommand productCreateCommand);
}
