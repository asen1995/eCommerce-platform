package com.ecommerence.platform.controller;

import com.ecommerence.platform.constants.AppConstants;
import com.ecommerence.platform.exception.*;
import com.ecommerence.platform.model.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(new ErrorMessage(AppConstants.PAIR_OF_PRODUCT_CATEGORY_SHOULD_BE_UNIQUE_ERROR_MESSAGE, new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ProductNotFoundException.class, OrderNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNotFound(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({OrderHaveAlreadyBeenApprovedException.class})
    public ResponseEntity<ErrorMessage> handleOrderHaveAlreadyBeenApprovedException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.OK);
    }

    @ExceptionHandler({ProductQuantityNotEnoughException.class, OrderHaveAlreadyBeenDeclinedException.class})
    public ResponseEntity<ErrorMessage> handleProductQuantityNotEnoughException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }
}
