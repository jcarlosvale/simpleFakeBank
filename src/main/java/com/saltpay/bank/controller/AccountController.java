package com.saltpay.bank.controller;

import com.saltpay.bank.dto.request.RequestAccountDTO;
import com.saltpay.bank.dto.response.ResponseAccountBalanceDTO;
import com.saltpay.bank.dto.response.ResponseAccountDTO;
import com.saltpay.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AccountController {

    public static final String ACCOUNT_END_POINT_V1 = "/v1/accounts";

    private final AccountService accountService;

    @GetMapping(
            path     = ACCOUNT_END_POINT_V1 + "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
            )
    public ResponseEntity<ResponseAccountBalanceDTO> getBalance(@PathVariable("id") final long accountId) {
        return ResponseEntity.ok(accountService.retrieveBalance(accountId));
    }

    @PostMapping(
            path     = ACCOUNT_END_POINT_V1,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> postAccount(@Valid @RequestBody RequestAccountDTO requestAccountDTO) {
        ResponseAccountDTO responseAccountDTO = accountService.createNewAccount(requestAccountDTO);

        URI uri =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(responseAccountDTO.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }
}
