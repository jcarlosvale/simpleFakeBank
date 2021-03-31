package com.saltpay.bank.controller;

import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OperationController {
    public static final String OPERATION_END_POINT_V1 = "/v1/operations";

    private final OperationService operationService;

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
