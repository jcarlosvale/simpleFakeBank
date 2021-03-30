package com.saltpay.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saltpay.bank.configuration.BankConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class RequestAccountDTO {
    @JsonProperty("user_id")
    @NotNull
    @Min(value = 1, message = BankConstants.INVALID_USER_ID)
    private Long userId;

    @JsonProperty("initial_amount")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false,  message = BankConstants.INVALID_INITIAL_AMOUNT)
    @Digits(integer = 9, fraction = 2)
    private BigDecimal initialDepositAmount;
}
