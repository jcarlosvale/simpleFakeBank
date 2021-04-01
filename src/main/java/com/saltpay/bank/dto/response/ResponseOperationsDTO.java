package com.saltpay.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ResponseOperationsDTO {
    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("operations")
    private List<ResponseOperationDTO> operationDTOList = new ArrayList<>();

    @JsonProperty("created_at")
    private LocalDateTime creationTimestamp;
}
