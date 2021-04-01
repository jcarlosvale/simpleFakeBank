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
@ApiModel(value="Transfer response", description = "Information after a transfer has been done.")
public class ResponseOperationDTO {

    @JsonProperty("operation_id")
    @ApiModelProperty(notes = "Transfer id.")
    private Long id;

    @JsonProperty("sender_account_id")
    @ApiModelProperty(notes = "Sender account id.")
    private Long senderAccountId;

    @JsonProperty("receiver_account_id")
    @ApiModelProperty(notes = "Receiver account id.")
    private Long receiverAccountId;

    @JsonProperty("value")
    @ApiModelProperty(notes = "Transferred value.")
    private BigDecimal value;

    @JsonProperty("created_at")
    @ApiModelProperty(notes = "Transfer date time.")
    private LocalDateTime creationTimestamp;
}
