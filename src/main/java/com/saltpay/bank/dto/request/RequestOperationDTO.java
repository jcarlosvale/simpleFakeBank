package com.saltpay.bank.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="Transfer request", description = "Sender/receiver accounts ids and value to be processed.")
public class RequestOperationDTO {
    @JsonProperty("sender_account_id")
    @NotNull
    @Min(value = 1, message = INVALID_ACCOUNT_ID)
    @ApiModelProperty(notes = "Sender account id.")
    private Long senderAccountId;

    @JsonProperty("receiver_account_id")
    @NotNull
    @Min(value = 1, message = INVALID_ACCOUNT_ID)
    @ApiModelProperty(notes = "Receiver account id.")
    private Long receiverAccountId;

    @JsonProperty("value")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false,  message = INVALID_VALUE)
    @Digits(integer = 12, fraction = 2, message = INVALID_VALUE_FORMAT)
    @ApiModelProperty(notes = "Amount to be transferred from sender account to receiver account.")
    private BigDecimal value;
}
