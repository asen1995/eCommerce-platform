package com.ecommerence.platform.mapper;

import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    Product toProductEntity(ProductDto productDto);
}
