package com.ecommerence.platform.exception;

public class OrderHaveAlreadyBeenDeclinedException extends Exception {
    public OrderHaveAlreadyBeenDeclinedException(String message) {
        super(message);
    }
}
