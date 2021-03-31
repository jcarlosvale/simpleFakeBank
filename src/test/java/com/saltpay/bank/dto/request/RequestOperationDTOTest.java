package com.saltpay.bank.dto.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static com.saltpay.bank.configuration.BankConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestOperationDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validDataTest() {
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(1L)
                        .receiverAccountId(1L)
                        .value(BigDecimal.valueOf(0.01))
                        .build();

        Set<ConstraintViolation<RequestOperationDTO>> violations = validator.validate(requestOperationDTO);

        assertThat(violations.size()).isZero();
    }

    @Test
    void invalidSenderAccountIdTest() {
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(0L)
                        .receiverAccountId(1L)
                        .value(BigDecimal.valueOf(0.01))
                        .build();

        Set<ConstraintViolation<RequestOperationDTO>> violations = validator.validate(requestOperationDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_ACCOUNT_ID));
    }

    @Test
    void invalidReceiverAccountIdTest() {
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(1L)
                        .receiverAccountId(-1L)
                        .value(BigDecimal.valueOf(0.01))
                        .build();

        Set<ConstraintViolation<RequestOperationDTO>> violations = validator.validate(requestOperationDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_ACCOUNT_ID));
    }

    @Test
    void invalidValueTest() {
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(1L)
                        .receiverAccountId(1L)
                        .value(BigDecimal.valueOf(0))
                        .build();

        Set<ConstraintViolation<RequestOperationDTO>> violations = validator.validate(requestOperationDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_VALUE));
    }

    @Test
    void invalidInitialAmountFractionDigitsLessTest() {
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(1L)
                        .receiverAccountId(1L)
                        .value(new BigDecimal("0.001"))
                        .build();

        Set<ConstraintViolation<RequestOperationDTO>> violations = validator.validate(requestOperationDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_VALUE_FORMAT));
    }

}
