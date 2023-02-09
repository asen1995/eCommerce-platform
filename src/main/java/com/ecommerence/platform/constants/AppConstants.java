package com.ecommerence.platform.constants;

public class AppConstants {
    public static final String QTY_MUST_BE_GREATER_THAN_ZERO_MESSAGE = "Quantity must be greater than 0";
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
    public static final String PRODUCT_SUCCESSFUL_ORDER_MESSAGE_TEMPLATE = "You successfully ordered %s %s";
    public static final String PRODUCT_QUANTITY_NOT_ENOUGH_MESSAGE_TEMPLATE = "Your order is %s of Product %s, but there are only %s left";
    public static final String PRODUCT_NAME_SIZE_VALIDATION_ERROR_MESSAGE = "Name must be between 3 and 50 characters";
    public static final String PRODUCT_CATEGORY_SIZE_VALIDATION_ERROR_MESSAGE = "Category must be between 3 and 30 characters";
    public static final String PRODUCT_DESCRIPTION_SIZE_VALIDATION_ERROR_MESSAGE = "Description must be less than 250 characters";

    public static final String PRODUCT_NAME_IS_MANDATORY_MESSAGE = "Name is mandatory";
    public static final String PRODUCT_CATEGORY_IS_MANDATORY_MESSAGE = "Category is mandatory";

    public static final String PRODUCT_QUANTITY_IS_MANDATORY_MESSAGE = "Quantity is mandatory";


    public static final String PRODUCT_WITH_ID_NOT_FOUND_MESSAGE_TEMPLATE = "Product with id %d does not exist";
    public static final String PAIR_OF_PRODUCT_CATEGORY_SHOULD_BE_UNIQUE_ERROR_MESSAGE = "The pair of product name and category should be unique!";
    public static final String CUSTOMER_NAME_IS_MANDATORY_MESSAGE = "Customer name is mandatory";
    public static final String CUSTOMER_PASSWORD_IS_MANDATORY_MESSAGE = "Customer password is mandatory";
    public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found";
    public static final String ORDER_NOT_FOUND_MESSAGE = "Order not found";
    public static final String ORDER_NAME_IS_MANDATORY_MESSAGE = "Order name is mandatory";
    public static final String ORDER_NAME_SIZE_VALIDATION_ERROR_MESSAGE = "Order name must be between 3 and 50 characters";
    public static final String ORDER_COMMENT_IS_MANDATORY_MESSAGE = "Order comment is mandatory";
    public static final String ORDER_COMMENT_SIZE_VALIDATION_ERROR_MESSAGE = "Order comment must be between 20 and 250 characters";
    public static final String ORDER_MUST_CONTAIN_AT_LEAST_ONE_PRODUCT_MESSAGE = "Order must contain at least one pair of product id and quantity";
    public static final String CUSTOMER_ID_CANNOT_BE_NULL_MESSAGE = "Customer id cannot be null";
}
