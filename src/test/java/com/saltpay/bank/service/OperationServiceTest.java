package com.saltpay.bank.service;

import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.Operation;
import com.saltpay.bank.exception.InvalidRequestOperationException;
import com.saltpay.bank.repository.OperationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OperationServiceTest {
    @Mock
    private AccountService accountService;
    @Mock
    private OperationRepository operationRepository;

    @Spy
    @InjectMocks
    private OperationService operationService;

    @Test
    void testCreateNewOperationSuccessfully() {
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        Account sender = Account.builder().id(1L).balance(BigDecimal.valueOf(1.01)).build();
        Account receiver = Account.builder().id(2L).balance(BigDecimal.valueOf(0.99)).build();
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(sender.getId())
                        .receiverAccountId(receiver.getId())
                        .value(someValue).build();
        Operation operationWithoutId =
                Operation.builder()
                        .senderAccount(sender)
                        .receiverAccount(receiver)
                        .value(someValue)
                        .operationDateTime(LocalDateTime.MIN).build();
        Operation operationWithId =
                Operation.builder().id(3L).senderAccount(sender).receiverAccount(receiver).value(someValue)
                        .operationDateTime(LocalDateTime.MIN).build();
        ResponseOperationDTO expectedResponseOperationDTO =
                ResponseOperationDTO.builder().id(operationWithId.getId()).senderAccountId(sender.getId())
                        .receiverAccountId(receiver.getId()).value(someValue)
                        .creationTimestamp(operationWithId.getOperationDateTime()).build();
        when(accountService.getAccountById(sender.getId())).thenReturn(sender);
        when(accountService.getAccountById(receiver.getId())).thenReturn(receiver);
        when(operationRepository.save(operationWithoutId)).thenReturn(operationWithId);
        when(operationService.getCurrentTimestamp()).thenReturn(LocalDateTime.MIN);

        ResponseOperationDTO actualResponseOperationDTO = operationService.createNewOperation(requestOperationDTO);

        Assertions.assertThat(actualResponseOperationDTO).isEqualTo(expectedResponseOperationDTO);
    }

    @Test
    void testCreateNewAccountWithNullRequest() {
        Throwable throwable = Assertions.catchThrowable(() ->operationService.createNewOperation(null));
        Assertions.assertThat(throwable).isInstanceOf(InvalidRequestOperationException.class);
    }
}
