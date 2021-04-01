package com.saltpay.bank.service.mapper;

import com.saltpay.bank.dto.UserDTO;
import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseAccountDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.Operation;
import com.saltpay.bank.entity.User;

public class BankMapper {
    public static Account toEntity(RequestAccountDTO requestAccountDTO) {
        return Account
                .builder()
                .initialDepositAmount(requestAccountDTO.getInitialDepositAmount())
                .balance(requestAccountDTO.getInitialDepositAmount())
                .build();
    }

    public static UserDTO toResponseOperationDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static ResponseAccountDTO toResponseOperationDTO(Account account) {
        return ResponseAccountDTO
                .builder()
                .userId(account.getUser().getId())
                .id(account.getId())
                .balance(account.getBalance())
                .creationTimestamp(account.getCreationTimestamp())
                .build();
    }

    public static Operation toEntity(RequestOperationDTO requestOperationDTO) {
        return Operation
                .builder()
                .value(requestOperationDTO.getValue())
                .build();
    }

    public static ResponseOperationDTO toResponseOperationDTO(Operation operation) {
        return ResponseOperationDTO
                .builder()
                .id(operation.getId())
                .senderAccountId(operation.getSenderAccount().getId())
                .receiverAccountId(operation.getReceiverAccount().getId())
                .value(operation.getValue())
                .creationTimestamp(operation.getOperationDateTime())
                .build();
    }
}
