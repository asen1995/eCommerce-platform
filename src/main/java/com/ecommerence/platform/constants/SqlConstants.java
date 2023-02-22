package com.ecommerence.platform.constants;

public class SqlConstants {
    public static final String GET_PRODUCT_BY_ID_FOR_UPDATE = "SELECT * FROM product WHERE id = :id FOR UPDATE";
    public static final String GET_DISTINCT_CATEGORIES_AND_AVAILABLE_PRODUCTS = "SELECT product_category as category , SUM(product_quantity) as availableProducts FROM product GROUP BY product_category";
    public static final String GET_ORDERS_FOR_AUTOMATIC_DECLINE = "SELECT * FROM ORDERS WHERE order_approved is null AND created_date < SYSDATE - INTERVAL '10' MINUTE FOR UPDATE";

    public static final String GET_ORDER_BY_ID_FOR_UPDATE = "SELECT * FROM orders WHERE id = :id  FOR UPDATE";
    public static final String GET_PRODUCTS_FOR_UPDATE = "SELECT * FROM product WHERE id IN (:ids) FOR UPDATE";
    public static final String UPSERT_PRODUCT_WITH_UNIQUE_NAME_AND_CATEGORY = "MERGE INTO PRODUCT p USING DUAL " +
            "ON (p.PRODUCT_NAME = :#{#product.name} AND p.PRODUCT_CATEGORY = :#{#product.category}) " +
            " WHEN MATCHED THEN " +
            "    UPDATE SET p.PRODUCT_QUANTITY = p.PRODUCT_QUANTITY + :#{#product.quantity} , p.UPDATED_DATE = SYSDATE " +
            " WHEN NOT MATCHED THEN " +
            "    INSERT (p.id, p.PRODUCT_NAME, p.PRODUCT_CATEGORY, p.PRODUCT_DESCRIPTION, p.PRODUCT_QUANTITY , p.CREATED_DATE) " +
            "    VALUES (product_id_seq.nextval , :#{#product.name}, :#{#product.category}, :#{#product.description}, :#{#product.quantity}, :#{#product.createdDate} ) ";
}
