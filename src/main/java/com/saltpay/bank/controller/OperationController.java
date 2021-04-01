package com.saltpay.bank.controller;

import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationsDTO;
import com.saltpay.bank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Operation Controller exposing 2 endpoints:
 * - GET
 *      given and account ID, retrieves your operations
 * - POST
 *      creates a new transfer operation receiving in your body the sender and receiver accounts ids and the value used
 *      in the transaction
 */
@RestController
@RequiredArgsConstructor
public class OperationController {

    public static final String OPERATION_END_POINT_V1 = "/v1/operations";
    public static final String OPERATION_GET_END_POINT_V1 = OPERATION_END_POINT_V1 + "/fromAccount/{accountId}";

    private final OperationService operationService;

    @GetMapping(
            path     = OPERATION_GET_END_POINT_V1,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseOperationsDTO> getOperations(@PathVariable("accountId") final long accountId) {
        return ResponseEntity.ok(operationService.retrieveOperations(accountId));
    }

    @PostMapping(
            path = OPERATION_END_POINT_V1,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> postAccount(@Valid @RequestBody RequestOperationDTO requestOperationDTO) {
        ResponseOperationDTO responseOperationDTO = operationService.createNewOperation(requestOperationDTO);

        URI uri =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(responseOperationDTO.getId())
                        .toUri();

        return ResponseEntity.created(uri).build();
    }
}
