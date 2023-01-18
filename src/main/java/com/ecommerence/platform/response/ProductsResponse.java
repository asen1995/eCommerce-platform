package com.ecommerence.platform.response;

import com.ecommerence.platform.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductsResponse {

    private List<Product> products;
    private Long totalElements;
}
