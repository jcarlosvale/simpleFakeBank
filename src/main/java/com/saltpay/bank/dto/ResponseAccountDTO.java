package com.saltpay.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ResponseAccountDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("account_id")
    private Long id;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("created_at")
    private LocalDateTime creationTimestamp;
}
