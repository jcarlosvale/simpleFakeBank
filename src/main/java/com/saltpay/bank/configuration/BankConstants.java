package com.saltpay.bank.configuration;

public final class BankConstants {

    /*
    validation messages
     */
    public static final String INVALID_USER_ID = "Invalid user_id";
    public static final String INVALID_INITIAL_AMOUNT = "Invalid initial_amount";
    public static final String INVALID_INITIAL_AMOUNT_FORMAT = "Invalid initial amount format";

    /*
    error messages
     */
    public static final String ERROR_MESSAGE_NULL_REQUEST_ACCOUNT_DTO = "Request account dto is null";
    public static final String ERROR_USER_NOT_FOUND = "User not found id={}";

    private BankConstants() {}
}
