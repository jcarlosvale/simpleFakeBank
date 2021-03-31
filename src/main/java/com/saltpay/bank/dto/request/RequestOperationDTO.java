package com.saltpay.bank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.saltpay.bank.configuration.BankConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestOperationDTO {
    @JsonProperty("sender_account_id")
    @NotNull
    @Min(value = 1, message = INVALID_ACCOUNT_ID)
    private Long senderAccountId;

    @JsonProperty("receiver_account_id")
    @NotNull
    @Min(value = 1, message = INVALID_ACCOUNT_ID)
    private Long receiverAccountId;

    @JsonProperty("value")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false,  message = INVALID_VALUE)
    @Digits(integer = 12, fraction = 2, message = INVALID_VALUE_FORMAT)
    private BigDecimal value;
}
