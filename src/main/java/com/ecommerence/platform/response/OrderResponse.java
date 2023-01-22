package com.ecommerence.platform.response;

import com.ecommerence.platform.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

    private String message;
    private Product product;
}
