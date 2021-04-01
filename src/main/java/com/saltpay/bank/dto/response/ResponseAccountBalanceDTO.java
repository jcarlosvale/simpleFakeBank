package com.saltpay.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="Account balance response", description = "The balance of an account")
public class ResponseAccountBalanceDTO {
    @JsonProperty("account_id")
    @ApiModelProperty(notes = "Account id.")
    private Long id;

    @JsonProperty("balance")
    @ApiModelProperty(notes = "Balance of the account.")
    private BigDecimal balance;

    @JsonProperty("created_at")
    @ApiModelProperty(notes = "Date and time when the balance was collected.")
    private LocalDateTime creationTimestamp;
}
