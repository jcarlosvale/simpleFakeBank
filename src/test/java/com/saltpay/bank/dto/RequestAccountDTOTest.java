package com.saltpay.bank.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static com.saltpay.bank.configuration.BankConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestAccountDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validUserIdTest() {
        RequestAccountDTO requestAccountDTO = new RequestAccountDTO(1L, new BigDecimal("0.01"));

        Set<ConstraintViolation<RequestAccountDTO>> violations = validator.validate(requestAccountDTO);

        assertThat(violations.size()).isZero();
    }

    @Test
    void invalidUserIdTest() {
        RequestAccountDTO requestAccountDTO = new RequestAccountDTO(0L, new BigDecimal("10.21"));

        Set<ConstraintViolation<RequestAccountDTO>> violations = validator.validate(requestAccountDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_USER_ID));
    }

    @Test
    void invalidInitialAmountTest() {
        RequestAccountDTO requestAccountDTO = new RequestAccountDTO(1L, new BigDecimal("0.00"));

        Set<ConstraintViolation<RequestAccountDTO>> violations = validator.validate(requestAccountDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_INITIAL_AMOUNT));
    }

    @Test
    void invalidInitialAmountFractionDigitsLessTest() {
        RequestAccountDTO requestAccountDTO = new RequestAccountDTO(1L, new BigDecimal("1.012"));

        Set<ConstraintViolation<RequestAccountDTO>> violations = validator.validate(requestAccountDTO);

        assertThat(violations.size()).isEqualTo(1);
        violations.forEach(action -> assertThat(action.getMessage())
                .isEqualTo(INVALID_INITIAL_AMOUNT_FORMAT));
    }
}
