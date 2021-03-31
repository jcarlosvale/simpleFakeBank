package com.saltpay.bank.service;

import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.response.ResponseAccountBalanceDTO;
import com.saltpay.bank.dto.response.ResponseAccountDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.exception.*;
import com.saltpay.bank.repository.AccountRepository;
import com.saltpay.bank.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.saltpay.bank.configuration.BankConstants.*;
import static com.saltpay.bank.service.ServiceUtil.throwsOnCondition;
import static com.saltpay.bank.service.mapper.BankMapper.toDTO;
import static com.saltpay.bank.service.mapper.BankMapper.toEntity;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ResponseAccountDTO createNewAccount(@Valid RequestAccountDTO requestAccountDTO) {
        log.debug("Creating a new account - {}", requestAccountDTO);
        throwsOnCondition(Objects.isNull(requestAccountDTO), InvalidRequestAccountException::new,
                ERROR_MESSAGE_NULL_REQUEST_ACCOUNT_DTO);
        User user = getUserById(requestAccountDTO.getUserId());
        Account account = toEntity(requestAccountDTO);
        fillMissingFields(account, user);
        account = accountRepository.save(account);
        log.debug("Created account - {}", account);
        return toDTO(account);
    }

    public ResponseAccountBalanceDTO retrieveBalance(Long accountId) {
        log.debug("Retrieving balance from accountId = {}", accountId);
        Account account = getAccountById(accountId);
        return ResponseAccountBalanceDTO.builder()
                .userDTO(toDTO(account.getUser()))
                .id(account.getId())
                .balance(account.getBalance())
                .creationTimestamp(getCurrentTimestamp())
                .build();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void transfer(@NonNull Account senderAccount, @NonNull Account receiverAccount, BigDecimal value) {
        log.debug("Starting transfer senderAccount: [{}] receiverAccount: [{}] value: [{}]",
                senderAccount, receiverAccount, value);
        throwsOnCondition(senderAccount.getBalance().compareTo(value) < 0,
                InsufficientBalanceException::new,
                String.format(ERROR_INSUFFICIENT_BALANCE, senderAccount.getId()));
        throwsOnCondition(senderAccount.equals(receiverAccount), TransferNotAllowedException::new);
        senderAccount.setBalance(senderAccount.getBalance().subtract(value));
        receiverAccount.setBalance(receiverAccount.getBalance().add(value));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        log.debug("Executed transfer senderAccount: [{}] receiverAccount: [{}] value: [{}]",
                senderAccount, receiverAccount, value);
    }

    Account getAccountById(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> {
                    log.error(ERROR_ACCOUNT_NOT_FOUND, accountId);
                    throw new AccountNotFoundException();
                });
    }

    private void fillMissingFields(Account account, User user) {
        account.setUser(user);
        account.setBalance(account.getInitialDepositAmount());
        account.setCreationTimestamp(getCurrentTimestamp());
    }

    private User getUserById(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> {
                    log.error(ERROR_USER_NOT_FOUND, userId);
                    throw new UserNotFoundException();
                });
    }

    LocalDateTime getCurrentTimestamp() {
            return LocalDateTime.now();
    }
}
