package com.saltpay.bank.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value="Customer/owner account information", description = "Id and name of a customer")
public class UserDTO {
    @ApiModelProperty(notes = "Customer id")
    private Long id;

    @ApiModelProperty(notes = "Customer's name")
    private String name;
}
