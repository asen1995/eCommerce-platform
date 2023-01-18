package com.ecommerence.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    private String category;
    private Integer productsAvailable;
}
