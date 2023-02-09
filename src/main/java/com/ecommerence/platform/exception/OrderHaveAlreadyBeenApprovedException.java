package com.ecommerence.platform.exception;

public class OrderHaveAlreadyBeenApprovedException extends Exception {
    public OrderHaveAlreadyBeenApprovedException(String message) {
        super(message);
    }
}
