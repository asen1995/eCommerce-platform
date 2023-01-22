package com.ecommerence.platform.constants;

public class SqlConstants {
    public static final String GET_PRODUCT_BY_ID_FOR_UPDATE = "SELECT * FROM product WHERE id = :id FOR UPDATE";
    public static final String GET_DISTINCT_CATEGORIES_AND_AVAILABLE_PRODUCTS = "SELECT product_category as category , SUM(product_quantity) as availableProducts FROM product GROUP BY product_category";
}
