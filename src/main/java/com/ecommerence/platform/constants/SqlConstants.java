package com.ecommerence.platform.constants;

public class SqlConstants {
    public static final String GET_PRODUCT_BY_ID_FOR_UPDATE = "SELECT * FROM product WHERE id = :id FOR UPDATE";
    public static final String GET_DISTINCT_CATEGORIES_AND_AVAILABLE_PRODUCTS = "SELECT product_category as category , SUM(product_quantity) as availableProducts FROM product GROUP BY product_category";
    public static final String GET_ORDERS_FOR_AUTOMATIC_DECLINE = "SELECT * FROM ORDERS WHERE order_approved is null AND created_date < SYSDATE - INTERVAL '10' MINUTE";
    public static final String GET_ORDERS_CONTAINING_STRING = "SELECT *\n" +
            "FROM Orders \n" +
            "JOIN Customer ON orders.customer_id = customer.id\n" +
            "WHERE\n" +
            "  Customer.customer_username LIKE  '%' || :search || '%' OR\n" +
            "  Customer.customer_first_name LIKE  '%' || :search || '%'  OR\n" +
            "  Customer.customer_last_name LIKE  '%' || :search || '%'  OR\n" +
            "  Orders.id LIKE  '%' || :search || '%'  OR\n" +
            "  Orders.order_comment LIKE  '%' || :search || '%' \n";
}
