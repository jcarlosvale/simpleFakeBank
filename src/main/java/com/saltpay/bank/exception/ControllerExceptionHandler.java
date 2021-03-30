package com.saltpay.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException re) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidRequestAccountException.class)
    public ResponseEntity<Void> handleInvalidRequestAccountException(InvalidRequestAccountException re) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
