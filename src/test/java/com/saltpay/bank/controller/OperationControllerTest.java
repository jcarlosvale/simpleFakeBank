package com.saltpay.bank.controller;

import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationsDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.Operation;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.repository.AccountRepository;
import com.saltpay.bank.repository.OperationRepository;
import com.saltpay.bank.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.saltpay.bank.controller.OperationController.OPERATION_END_POINT_V1;
import static com.saltpay.bank.controller.OperationController.OPERATION_GET_END_POINT_V1;
import static com.saltpay.bank.controller.TestUtil.extractId;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class OperationControllerTest {

    private RestTemplate restTemplate;
    private String url;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    private int randomServerPort = 0;

    @BeforeEach
    public void beforeTest() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" +  randomServerPort;
    }

    @Test
    void createNewOperationValidSameOwnerTest() {
        LocalDateTime current = LocalDateTime.now();
        User user = userRepository.findById(1L).get();
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        Account senderAccount = createAccount(user, BigDecimal.valueOf(1.01));
        Account receiverAccount = createAccount(user, BigDecimal.valueOf(1.99));
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(senderAccount.getId())
                        .receiverAccountId(receiverAccount.getId())
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request =
                new HttpEntity<>(requestOperationDTO);

        ResponseEntity<Void> response = restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isNotNull();

        Long objectId = extractId(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.LOCATION)).get(0),
                url + OPERATION_END_POINT_V1);

        Operation actualOperation = operationRepository.findById(objectId).orElse(Operation.builder().build());
        Assertions.assertThat(actualOperation.getValue()).isEqualTo(someValue);
        Assertions.assertThat(actualOperation.getOperationDateTime()).isAfter(current);
        Assertions.assertThat(actualOperation.getSenderAccount().getBalance()).isEqualTo("1.00");
        Assertions.assertThat(actualOperation.getReceiverAccount().getBalance()).isEqualTo("2.00");
    }

    @Test
    void createNewOperationValidDifferentOwnerTest() {
        LocalDateTime current = LocalDateTime.now();
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        Account senderAccount = createAccount(user1, BigDecimal.valueOf(0.01));
        Account receiverAccount = createAccount(user2, BigDecimal.valueOf(0.99));
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(senderAccount.getId())
                        .receiverAccountId(receiverAccount.getId())
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request =
                new HttpEntity<>(requestOperationDTO);

        ResponseEntity<Void> response = restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isNotNull();

        Long objectId = extractId(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.LOCATION)).get(0),
                url + OPERATION_END_POINT_V1);

        Operation actualOperation = operationRepository.findById(objectId).orElse(Operation.builder().build());
        Assertions.assertThat(actualOperation.getValue()).isEqualTo(someValue);
        Assertions.assertThat(actualOperation.getOperationDateTime()).isAfter(current);
        Assertions.assertThat(actualOperation.getSenderAccount().getBalance()).isEqualTo("0.00");
        Assertions.assertThat(actualOperation.getReceiverAccount().getBalance()).isEqualTo("1.00");
    }

    @Test
    void createNewOperationSenderNotFoundTest() {
        User user = userRepository.findById(2L).get();
        Account receiverAccount = createAccount(user, BigDecimal.valueOf(0.99));
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(Long.MAX_VALUE)
                        .receiverAccountId(receiverAccount.getId())
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request =
                new HttpEntity<>(requestOperationDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createNewOperationReceiverNotFoundTest() {
        User user = userRepository.findById(1L).get();
        Account senderAccount = createAccount(user, BigDecimal.valueOf(0.99));
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(senderAccount.getId())
                        .receiverAccountId(Long.MAX_VALUE)
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request =
                new HttpEntity<>(requestOperationDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createNewOperationInsufficientFundsTest() {
        User user = userRepository.findById(1L).get();
        BigDecimal someValue = BigDecimal.valueOf(0.02);
        Account senderAccount = createAccount(user, BigDecimal.valueOf(0.01));
        Account receiverAccount = createAccount(user, BigDecimal.valueOf(1.99));
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(senderAccount.getId())
                        .receiverAccountId(receiverAccount.getId())
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request = new HttpEntity<>(requestOperationDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createNewOperationUsingSameAccountTest() {
        User user = userRepository.findById(1L).get();
        BigDecimal someValue = BigDecimal.valueOf(0.01);
        Account senderAccount = createAccount(user, BigDecimal.valueOf(1.01));
        RequestOperationDTO requestOperationDTO =
                RequestOperationDTO.builder()
                        .senderAccountId(senderAccount.getId())
                        .receiverAccountId(senderAccount.getId())
                        .value(someValue)
                        .build();

        HttpEntity<RequestOperationDTO> request =
                new HttpEntity<>(requestOperationDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + OPERATION_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getOperationsSuccessfulTest() {
        BigDecimal someValue1 = new BigDecimal("1.50");
        BigDecimal someValue2 = new BigDecimal("0.50");

        Account account1 = createAccount(userRepository.findById(1L).get(), someValue1);
        Account account2 = createAccount(userRepository.findById(2L).get(), someValue2);

        Operation latestOperation =
                Operation.builder()
                        .senderAccount(account2)
                        .receiverAccount(account1)
                        .value(someValue2)
                        .operationDateTime(LocalDateTime.MAX)
                        .build();
        operationRepository.save(latestOperation);
        ResponseOperationDTO latestOperationDTO =
                ResponseOperationDTO.builder()
                        .id(latestOperation.getId())
                        .senderAccountId(latestOperation.getSenderAccount().getId())
                        .receiverAccountId(latestOperation.getReceiverAccount().getId())
                        .value(latestOperation.getValue())
                        .build();

        Operation earliestOperation =
                Operation.builder()
                        .senderAccount(account1)
                        .receiverAccount(account2)
                        .value(someValue1)
                        .operationDateTime(LocalDateTime.MIN)
                        .build();
        operationRepository.save(earliestOperation);
        ResponseOperationDTO earliestOperationDTO =
                ResponseOperationDTO.builder()
                        .id(earliestOperation.getId())
                        .senderAccountId(earliestOperation.getSenderAccount().getId())
                        .receiverAccountId(earliestOperation.getReceiverAccount().getId())
                        .value(earliestOperation.getValue())
                        .build();

        ResponseEntity<ResponseOperationsDTO> response = restTemplate.getForEntity(url + OPERATION_GET_END_POINT_V1,
                ResponseOperationsDTO.class, account1.getId());
        ResponseOperationsDTO responseOperationsDTO = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assert responseOperationsDTO != null;
        Assertions.assertThat(responseOperationsDTO.getAccountId()).isEqualTo(account1.getId());
        Assertions.assertThat(responseOperationsDTO.getOperationDTOList().size()).isEqualTo(2);

        earliestOperationDTO.setCreationTimestamp(responseOperationsDTO.getOperationDTOList().get(0).getCreationTimestamp());
        Assertions.assertThat(responseOperationsDTO.getOperationDTOList().get(0)).isEqualTo(earliestOperationDTO);

        latestOperationDTO.setCreationTimestamp(responseOperationsDTO.getOperationDTOList().get(1).getCreationTimestamp());
        Assertions.assertThat(responseOperationsDTO.getOperationDTOList().get(1)).isEqualTo(latestOperationDTO);
    }

    @Test
    void testGetBalancesNotFoundAccount() {
        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.getForEntity(url + OPERATION_GET_END_POINT_V1,
                ResponseOperationsDTO.class, Long.MAX_VALUE));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getEmptyOperationsSuccessfulTest() {
        BigDecimal someValue1 = new BigDecimal("1.50");

        Account account1 = createAccount(userRepository.findById(1L).get(), someValue1);

        ResponseEntity<ResponseOperationsDTO> response = restTemplate.getForEntity(url + OPERATION_GET_END_POINT_V1,
                ResponseOperationsDTO.class, account1.getId());
        ResponseOperationsDTO responseOperationsDTO = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assert responseOperationsDTO != null;
        Assertions.assertThat(responseOperationsDTO.getAccountId()).isEqualTo(account1.getId());
        Assertions.assertThat(responseOperationsDTO.getOperationDTOList().isEmpty()).isTrue();
    }

    public Account createAccount(User user, BigDecimal initialDepositAmount) {
        Account account =
                Account.builder()
                        .initialDepositAmount(initialDepositAmount)
                        .creationTimestamp(LocalDateTime.now())
                        .balance(initialDepositAmount)
                        .user(user)
                        .build();
        accountRepository.save(account);
        return account;
    }
}
