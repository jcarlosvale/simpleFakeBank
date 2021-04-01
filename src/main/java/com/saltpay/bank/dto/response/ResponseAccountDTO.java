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
@ApiModel(value="New account created response", description = "The new account information.")
public class ResponseAccountDTO {
    @JsonProperty("user_id")
    @ApiModelProperty(notes = "Customer id or the account's owner.")
    private Long userId;

    @JsonProperty("account_id")
    @ApiModelProperty(notes = "The account id.")
    private Long id;

    @JsonProperty("balance")
    @ApiModelProperty(notes = "The account's balance.")
    private BigDecimal balance;

    @JsonProperty("created_at")
    @ApiModelProperty(notes = "Date and time when the account was created.")
    private LocalDateTime creationTimestamp;
}
