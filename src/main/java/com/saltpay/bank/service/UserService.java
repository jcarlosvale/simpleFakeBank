package com.saltpay.bank.service;

import com.saltpay.bank.dto.UserDTO;
import com.saltpay.bank.entity.User;
import com.saltpay.bank.repository.UserRepository;
import com.saltpay.bank.service.mapper.BankMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;

    public UserDTO findUserById(Long userId) {
        log.debug("Loading user id={}", userId);
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            return BankMapper.toDTO(user.get());
        }
        return null;
    }
}
