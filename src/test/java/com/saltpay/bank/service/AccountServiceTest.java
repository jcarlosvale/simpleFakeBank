package com.saltpay.bank.service;

import com.saltpay.bank.dto.RequestAccountDTO;
import com.saltpay.bank.dto.ResponseAccountDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.exception.InvalidRequestAccountException;
import com.saltpay.bank.exception.UserNotFoundException;
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
    public void testCreateNewAccountSuccessfully(){
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
        when(accountService.getCurrentTimestamp()).thenReturn(LocalDateTime.MIN);

        ResponseAccountDTO actualResponseAccountDTO = accountService.createNewAccount(requestAccountDTO);
        Assertions.assertThat(actualResponseAccountDTO).isEqualTo(expectedResponseAccountDTO);
    }


    @Test
    public void testCreateNewAccountWithNullRequest() {
        Throwable throwable = Assertions.catchThrowable(() ->accountService.createNewAccount(null));
        Assertions.assertThat(throwable).isInstanceOf(InvalidRequestAccountException.class);
    }

    @Test
    public void testCreateNewAccountWithNotFoundUserId() {
        Long someId = 1L;
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someId)
                        .build();
        when(userRepository.findById(someId)).thenReturn(Optional.empty());
        Throwable throwable = Assertions.catchThrowable(() ->accountService.createNewAccount(requestAccountDTO));
        Assertions.assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

}
