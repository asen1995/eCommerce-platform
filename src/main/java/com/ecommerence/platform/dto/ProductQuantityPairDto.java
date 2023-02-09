package com.ecommerence.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductQuantityPairDto {

    private Integer productId;
    private Integer quantity;
}
