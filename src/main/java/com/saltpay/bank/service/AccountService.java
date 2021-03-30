package com.saltpay.bank.service;

import com.saltpay.bank.configuration.BankConstants;
import com.saltpay.bank.dto.RequestAccountDTO;
import com.saltpay.bank.dto.ResponseAccountDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.exception.InvalidRequestAccountException;
import com.saltpay.bank.exception.UserNotFoundException;
import com.saltpay.bank.repository.AccountRepository;
import com.saltpay.bank.repository.UserRepository;
import com.saltpay.bank.service.mapper.BankMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public ResponseAccountDTO createNewAccount(@Valid RequestAccountDTO requestAccountDTO) {
        log.debug("Creating a new account - {}", requestAccountDTO);
        if (Objects.isNull(requestAccountDTO)) {
            log.error(BankConstants.ERROR_MESSAGE_NULL_REQUEST_ACCOUNT_DTO);
            throw new InvalidRequestAccountException();
        }
        Optional<User> user = userRepository.findById(requestAccountDTO.getUserId());
        if (user.isEmpty()) {
            log.error(BankConstants.ERROR_USER_NOT_FOUND, requestAccountDTO.getUserId());
            throw new UserNotFoundException();
        } else {
            Account account = BankMapper.toEntity(requestAccountDTO);
            account.setUser(user.get());
            account.setBalance(account.getInitialDepositAmount());
            account.setCreationTimestamp(getCurrentTimestamp());
            account = accountRepository.save(account);
            log.debug("Created account - {}", account);
            return BankMapper.toDTO(account);
        }
    }

    public LocalDateTime getCurrentTimestamp() {
            return LocalDateTime.now();
    }
}
