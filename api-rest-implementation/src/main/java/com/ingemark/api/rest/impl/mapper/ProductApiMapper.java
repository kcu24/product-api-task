package com.ingemark.api.rest.impl.mapper;

import com.ingemark.application.command.ProductCreateCommand;
import com.ingemark.api.rest.impl.dto.ProductRequest;
import com.ingemark.api.rest.impl.dto.ProductResponse;
import com.ingemark.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductApiMapper {
    ProductCreateCommand mapToProductCreateCommand(ProductRequest productRequest);

    ProductResponse mapToProductApiResponse(Product product);
}
