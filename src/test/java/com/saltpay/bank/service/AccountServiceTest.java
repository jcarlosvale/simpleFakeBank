package com.saltpay.bank.service;

import com.saltpay.bank.dto.UserDTO;
import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.response.ResponseAccountBalanceDTO;
import com.saltpay.bank.dto.response.ResponseAccountDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.exception.*;
import com.saltpay.bank.repository.AccountRepository;
import com.saltpay.bank.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private AccountService accountService;

    @Test
    void testCreateNewAccountSuccessfully(){
        Long someUserID = 1L;
        String someUserName = "someUserName";
        Long someAccountID = 2L;
        BigDecimal initialAmount = BigDecimal.valueOf(10.01);
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someUserID)
                        .initialDepositAmount(initialAmount)
                        .build();
        User user =
                User.builder()
                        .id(someUserID)
                        .name(someUserName)
                        .build();

        Account accountWithoutId =
                Account.builder()
                        .initialDepositAmount(initialAmount)
                        .creationTimestamp(LocalDateTime.MIN)
                        .balance(initialAmount)
                        .user(user)
                        .build();

        Account accountWithId =
                Account.builder()
                        .id(someAccountID)
                        .initialDepositAmount(initialAmount)
                        .creationTimestamp(LocalDateTime.MIN)
                        .balance(initialAmount)
                        .user(user)
                        .build();

        ResponseAccountDTO expectedResponseAccountDTO =
                ResponseAccountDTO.builder()
                        .userId(someUserID)
                        .id(someAccountID)
                        .balance(initialAmount)
                        .creationTimestamp(LocalDateTime.MIN)
                        .build();

        when(userRepository.findById(someUserID)).thenReturn(Optional.ofNullable(user));
        when(accountRepository.save(accountWithoutId)).thenReturn(accountWithId);
        doReturn(LocalDateTime.MIN).when(accountService).getCurrentTimestamp();

        ResponseAccountDTO actualResponseAccountDTO = accountService.createNewAccount(requestAccountDTO);
        Assertions.assertThat(actualResponseAccountDTO).isEqualTo(expectedResponseAccountDTO);
    }


    @Test
    void testCreateNewAccountWithNullRequest() {
        Throwable throwable = Assertions.catchThrowable(() ->accountService.createNewAccount(null));
        Assertions.assertThat(throwable).isInstanceOf(InvalidRequestAccountException.class);
    }

    @Test
    void testCreateNewAccountWithNotFoundUserId() {
        Long someId = 1L;
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someId)
                        .build();
        when(userRepository.findById(someId)).thenReturn(Optional.empty());
        Throwable throwable = Assertions.catchThrowable(() ->accountService.createNewAccount(requestAccountDTO));
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testGetAccountNotFound() {
        Long someId = 1L;
        when(accountRepository.findById(someId)).thenReturn(Optional.empty());
        Throwable throwable = Assertions.catchThrowable(() ->accountService.getAccountById(someId));
        Assertions.assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void testSuccessfulTransfer() {
        Account sender =
                Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(0.01))
                        .build();
        Account receiver =
                Account.builder()
                        .id(2L)
                        .balance(BigDecimal.valueOf(0.99))
                        .build();
        accountService.transfer(sender, receiver, BigDecimal.valueOf(0.01));
        Assertions.assertThat(sender.getBalance()).isEqualTo("0.00");
        Assertions.assertThat(receiver.getBalance()).isEqualTo("1.00");
    }

    @Test
    void testUnsuccessfulTransfer() {
        Account sender =
                Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(0.00))
                        .build();
        Account receiver =
                Account.builder()
                        .id(2L)
                        .balance(BigDecimal.valueOf(1.00))
                        .build();
        Throwable throwable = Assertions.catchThrowable(() ->accountService.transfer(sender, receiver, BigDecimal.valueOf(0.01)));
        Assertions.assertThat(throwable).isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void testNotAllowedSameAccountTransfer() {
        Account sender =
                Account.builder()
                        .id(1L)
                        .balance(BigDecimal.valueOf(1.00))
                        .build();
        Throwable throwable = Assertions.catchThrowable(() ->accountService.transfer(sender, sender, BigDecimal.valueOf(0.01)));
        Assertions.assertThat(throwable).isInstanceOf(TransferNotAllowedException.class);
    }

    @Test
    void testRetrieveBalanceSuccessful() {
        Long someAccountId = 2L;
        BigDecimal someBalance = BigDecimal.valueOf(1.11);
        User user =
                User.builder()
                        .id(1L)
                        .name("some name")
                        .build();
        Account account =
                Account.builder()
                        .id(someAccountId)
                        .initialDepositAmount(BigDecimal.valueOf(100))
                        .creationTimestamp(LocalDateTime.now())
                        .balance(someBalance)
                        .user(user)
                        .build();

        ResponseAccountBalanceDTO expectedResponse =
                ResponseAccountBalanceDTO.builder()
                        .id(account.getId())
                        .balance(account.getBalance())
                        .creationTimestamp(LocalDateTime.MIN)
                        .build();

        doReturn(LocalDateTime.MIN).when(accountService).getCurrentTimestamp();
        doReturn(account).when(accountService).getAccountById(someAccountId);

        ResponseAccountBalanceDTO actualResponse = accountService.retrieveBalance(someAccountId);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
