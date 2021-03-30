package com.saltpay.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException re) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERROR: " + re.getMessage());
    }

    //TODO: null pointer exception
    //TODO usernotfound exception 404
}
