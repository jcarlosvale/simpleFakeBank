package com.saltpay.bank.exception;

public class InvalidRequestOperationException extends IllegalArgumentException {
    public InvalidRequestOperationException() {
        super("Invalid request operation exception");
    }
}
