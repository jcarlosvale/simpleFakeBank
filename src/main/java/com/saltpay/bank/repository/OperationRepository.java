package com.saltpay.bank.repository;

import com.saltpay.bank.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<List<Operation>> findAllBySenderAccount_IdOrReceiverAccount_IdOrderByOperationDateTimeDesc(long senderAccountId, long receiverAccountId);
}
