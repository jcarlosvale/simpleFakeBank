package com.saltpay.bank.service.mapper;

import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseAccountDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.Operation;

public class BankMapper {
    public static Account toAccountEntity(RequestAccountDTO requestAccountDTO) {
        return Account
                .builder()
                .initialDepositAmount(requestAccountDTO.getInitialDepositAmount())
                .balance(requestAccountDTO.getInitialDepositAmount())
                .build();
    }

    public static ResponseAccountDTO toResponseAccountDTO(Account account) {
        return ResponseAccountDTO
                .builder()
                .userId(account.getUser().getId())
                .id(account.getId())
                .balance(account.getBalance())
                .creationTimestamp(account.getCreationTimestamp())
                .build();
    }

    public static Operation toOperationEntity(RequestOperationDTO requestOperationDTO) {
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
