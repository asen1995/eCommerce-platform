package com.ecommerence.platform;

import com.ecommerence.platform.response.CategoryAvailableProducts;

public class CategoryAvailableProductsMock implements CategoryAvailableProducts {
    @Override
    public String getCategory() {
        return "TV";
    }

    @Override
    public Integer getAvailableProducts() {
        return 30;
    }
}
