package com.saltpay.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="List of operations response", description = "List of operations collected given one account.")
public class ResponseOperationsDTO {

    @JsonProperty("account_id")
    @ApiModelProperty(notes = "Account id.")
    private Long accountId;

    @JsonProperty("operations")
    @ApiModelProperty(notes = "List of operations of the given account.")
    private List<ResponseOperationDTO> operationDTOList = new ArrayList<>();

    @JsonProperty("created_at")
    @ApiModelProperty(notes = "Date and time when these operation was collected.")
    private LocalDateTime creationTimestamp;
}
