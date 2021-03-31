package com.saltpay.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, AccountNotFoundException.class})
    public ResponseEntity<String> handleUserNotFoundException(Exception re) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re.getMessage());
    }

    @ExceptionHandler({InvalidRequestAccountException.class, InsufficientBalanceException.class,
            InvalidRequestOperationException.class, TransferNotAllowedException.class})
    public ResponseEntity<String> handleInvalidRequestAccountException(Exception re) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re.getMessage());
    }
}
