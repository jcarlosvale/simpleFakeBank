package com.saltpay.bank.controller;

import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.response.ResponseAccountBalanceDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.repository.AccountRepository;
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

import static com.saltpay.bank.controller.AccountController.ACCOUNT_END_POINT_V1;
import static com.saltpay.bank.controller.AccountController.ACCOUNT_GET_END_POINT_V1;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class AccountControllerTest {

    private RestTemplate restTemplate;
    private String url;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    @LocalServerPort
    private int randomServerPort = 0;

    @BeforeEach
    public void beforeTest() {
        restTemplate = new RestTemplate();
        url = "http://localhost:" +  randomServerPort;
    }

    @Test
    void createNewAccountValidTest() {
        LocalDateTime current = LocalDateTime.now();
        Long someUserID = 1L;
        BigDecimal initialAmount = BigDecimal.valueOf(0.01);
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someUserID)
                        .initialDepositAmount(initialAmount)
                        .build();
        HttpEntity<RequestAccountDTO> request =
                new HttpEntity<>(requestAccountDTO);

        ResponseEntity<Void> response = restTemplate.postForEntity(url + ACCOUNT_END_POINT_V1, request, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getHeaders().get(HttpHeaders.LOCATION)).isNotNull();

        Long objectId = extractId(Objects.requireNonNull(response.getHeaders().get(HttpHeaders.LOCATION)).get(0),
                url + ACCOUNT_END_POINT_V1);

        Account actualAccount = accountRepository.findById(objectId).orElse(new Account());
        Assertions.assertThat(actualAccount.getInitialDepositAmount()).isEqualTo(initialAmount);
        Assertions.assertThat(actualAccount.getCreationTimestamp()).isAfter(current);
        Assertions.assertThat(actualAccount.getBalance()).isEqualTo(initialAmount);
        Assertions.assertThat(actualAccount.getUser().getId()).isEqualTo(someUserID);
    }

    @Test
    void createNewAccountUsingNotFoundUserTest() {
        Long someUserID = Long.MAX_VALUE;
        BigDecimal initialAmount = BigDecimal.valueOf(10.01);
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someUserID)
                        .initialDepositAmount(initialAmount)
                        .build();
        HttpEntity<RequestAccountDTO> request =
                new HttpEntity<>(requestAccountDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + ACCOUNT_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createNewAccountUsingInvalidRequestTest() {
        Long someUserID = 1L;
        BigDecimal initialAmount = BigDecimal.valueOf(0.00);
        RequestAccountDTO requestAccountDTO =
                RequestAccountDTO.builder()
                        .userId(someUserID)
                        .initialDepositAmount(initialAmount)
                        .build();
        HttpEntity<RequestAccountDTO> request =
                new HttpEntity<>(requestAccountDTO);

        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.postForEntity(url + ACCOUNT_END_POINT_V1, request, Void.class));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAccountBalanceSuccessfulTest() {
        BigDecimal someBalance = BigDecimal.valueOf(0.01);
        User user = userRepository.findById(1L).get();
        Account account = createAccount(user, BigDecimal.valueOf(100), someBalance);
        ResponseAccountBalanceDTO expectedResponse =
                ResponseAccountBalanceDTO.builder()
                        .id(account.getId())
                        .balance(account.getBalance())
                        .build();
        ResponseEntity<ResponseAccountBalanceDTO> response = restTemplate.getForEntity(url + ACCOUNT_GET_END_POINT_V1, ResponseAccountBalanceDTO.class, account.getId());
        ResponseAccountBalanceDTO actualResponse = response.getBody();

        expectedResponse.setId(actualResponse.getId());
        expectedResponse.setCreationTimestamp(actualResponse.getCreationTimestamp());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void getAccountBalanceNotFoundTest() {
        Throwable throwable = Assertions.catchThrowable(() ->restTemplate.getForEntity(url + ACCOUNT_GET_END_POINT_V1, ResponseAccountBalanceDTO.class, Long.MAX_VALUE));
        Assertions.assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        Assertions.assertThat(((HttpClientErrorException) throwable).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    public Account createAccount(User user, BigDecimal initialDepositAmount, BigDecimal balance) {
        Account account =
                Account.builder()
                        .initialDepositAmount(initialDepositAmount)
                        .creationTimestamp(LocalDateTime.now())
                        .balance(balance)
                        .user(user)
                        .build();
        accountRepository.save(account);
        return account;
    }

    public static Long extractId(String locationUrl, String url) {
        url = url + "/";
        return Long.valueOf(locationUrl.replace(url, ""));
    }
}
