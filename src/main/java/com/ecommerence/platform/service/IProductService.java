package com.ecommerence.platform.service;

import com.ecommerence.platform.dto.ProductDto;
import com.ecommerence.platform.enums.DirectionEnum;
import com.ecommerence.platform.enums.ProductOrderEnum;
import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.response.ProductsResponse;

public interface IProductService {
    ProductDto createProduct(ProductDto productDto);

    void deleteProduct(Integer id) throws ProductNotFoundException;

    ProductDto updateProduct(Integer id, ProductDto productDto) throws ProductNotFoundException;

    ProductsResponse getProducts(ProductOrderEnum orderBy, DirectionEnum direction, Integer page, Integer pageSize);
}
