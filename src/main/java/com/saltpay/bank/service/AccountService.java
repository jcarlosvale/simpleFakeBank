package com.saltpay.bank.service;

import com.saltpay.bank.dto.RequestAccountDTO;
import com.saltpay.bank.dto.ResponseAccountDTO;
import com.saltpay.bank.entity.Account;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.exception.UserNotFoundException;
import com.saltpay.bank.repository.AccountRepository;
import com.saltpay.bank.repository.UserRepository;
import com.saltpay.bank.service.mapper.BankMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public ResponseAccountDTO createNewAccount(@Valid RequestAccountDTO requestAccountDTO) {
        log.debug("Creating a new account - {}", requestAccountDTO);
        Optional<User> user = userRepository.findById(requestAccountDTO.getUserId());
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        } else {
            Account account = BankMapper.toEntity(requestAccountDTO);
            account.setUser(user.get());
            account.setCreationTimestamp(LocalDateTime.now());
            accountRepository.save(account);
            log.debug("Created account - {}", account);
            return BankMapper.toDTO(account);
        }
    }
}
