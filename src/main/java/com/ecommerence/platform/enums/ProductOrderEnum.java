package com.ecommerence.platform.enums;

public enum ProductOrderEnum {
    NAME("name"), CATEGORY("category"), DESCRIPTION("description"), QUANTITY("quantity");

    private final String value;

    ProductOrderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
