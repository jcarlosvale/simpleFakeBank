package com.saltpay.bank.configuration;

public final class BankConstants {

    /*
    validation messages
     */
    public static final String INVALID_USER_ID = "Invalid user_id";
    public static final String INVALID_ACCOUNT_ID = "Invalid account_id";
    public static final String INVALID_INITIAL_AMOUNT = "Invalid initial_amount";
    public static final String INVALID_INITIAL_AMOUNT_FORMAT = "Invalid initial amount format";
    public static final String INVALID_VALUE = "Invalid value";
    public static final String INVALID_VALUE_FORMAT = "Invalid value format";

    /*
    error messages
     */
    public static final String ERROR_MESSAGE_NULL_REQUEST_ACCOUNT_DTO = "Request account dto is null";
    public static final String ERROR_MESSAGE_NULL_REQUEST_OPERATION_DTO = "Request operation dto is null";
    public static final String ERROR_USER_NOT_FOUND = "User not found id={}";
    public static final String ERROR_ACCOUNT_NOT_FOUND = "Account not found id={}";
    public static final String ERROR_INSUFFICIENT_BALANCE = "Insufficiente balance in Account id=%d";

    private BankConstants() {}
}
