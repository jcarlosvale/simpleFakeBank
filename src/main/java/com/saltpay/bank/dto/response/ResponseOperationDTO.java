package com.saltpay.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ResponseOperationDTO {
    @JsonProperty("operation_id")
    private Long id;

    @JsonProperty("sender_account_id")
    private Long senderAccountId;

    @JsonProperty("receiver_account_id")
    private Long receiverAccountId;

    @JsonProperty("value")
    private BigDecimal value;

    @JsonProperty("created_at")
    private LocalDateTime creationTimestamp;
}
