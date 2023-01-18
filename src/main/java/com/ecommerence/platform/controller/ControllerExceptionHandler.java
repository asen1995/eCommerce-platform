package com.ecommerence.platform.controller;

import com.ecommerence.platform.exception.ProductNotFoundException;
import com.ecommerence.platform.exception.ProductQuantityNotEnoughException;
import com.ecommerence.platform.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleProductNotFound(ProductNotFoundException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductQuantityNotEnoughException.class)
    public ResponseEntity<ErrorMessage> handleProductQuantityNotEnoughException(ProductQuantityNotEnoughException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }
}
