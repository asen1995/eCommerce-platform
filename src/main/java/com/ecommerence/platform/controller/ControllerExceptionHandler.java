package com.ecommerence.platform.controller;

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

}
