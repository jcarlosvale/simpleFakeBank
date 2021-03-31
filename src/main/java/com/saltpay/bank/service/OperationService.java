package com.saltpay.bank.service;

import com.saltpay.bank.dto.request.RequestOperationDTO;
import com.saltpay.bank.dto.response.ResponseOperationDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.Operation;
import com.saltpay.bank.exception.InvalidRequestOperationException;
import com.saltpay.bank.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.saltpay.bank.configuration.BankConstants.ERROR_MESSAGE_NULL_REQUEST_OPERATION_DTO;
import static com.saltpay.bank.service.ServiceUtil.throwsOnCondition;
import static com.saltpay.bank.service.mapper.BankMapper.toDTO;
import static com.saltpay.bank.service.mapper.BankMapper.toEntity;

@Service
@RequiredArgsConstructor
@Log4j2
public class OperationService {

    private final AccountService accountService;
    private final OperationRepository operationRepository;

    @Transactional
    public ResponseOperationDTO createNewOperation(@Valid RequestOperationDTO requestOperationDTO) {
        log.debug("Creating a new operation - {}", requestOperationDTO);
        throwsOnCondition(Objects.isNull(requestOperationDTO), InvalidRequestOperationException::new,
                ERROR_MESSAGE_NULL_REQUEST_OPERATION_DTO);
        Operation operation = toEntity(requestOperationDTO);
        Account senderAccount = accountService.getAccountById(requestOperationDTO.getSenderAccountId());
        Account receiverAccount = accountService.getAccountById(requestOperationDTO.getReceiverAccountId());
        accountService.transfer(senderAccount, receiverAccount, requestOperationDTO.getValue());
        fillMissingFields(operation, senderAccount, receiverAccount);
        operation = operationRepository.save(operation);
        log.debug("Created operation - {}", operation);
        return toDTO(operation);
    }

    private void fillMissingFields(Operation operation, Account senderAccount, Account receiverAccount) {
        operation.setSenderAccount(senderAccount);
        operation.setReceiverAccount(receiverAccount);
        operation.setOperationDateTime(getCurrentTimestamp());
    }

    LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }
}
