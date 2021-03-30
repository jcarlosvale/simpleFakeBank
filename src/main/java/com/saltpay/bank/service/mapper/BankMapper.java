package com.saltpay.bank.service.mapper;

import com.saltpay.bank.dto.RequestAccountDTO;
import com.saltpay.bank.dto.ResponseAccountDTO;
import com.saltpay.bank.dto.UserDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;

public class BankMapper {
    public static Account toEntity(RequestAccountDTO requestAccountDTO) {
        return Account
                .builder()
                .initialDepositAmount(requestAccountDTO.getInitialDepositAmount())
                .balance(requestAccountDTO.getInitialDepositAmount())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static ResponseAccountDTO toDTO(Account account) {
        return ResponseAccountDTO
                .builder()
                .userId(account.getUser().getId())
                .id(account.getId())
                .balance(account.getBalance())
                .creationTimestamp(account.getCreationTimestamp())
                .build();
    }
}
