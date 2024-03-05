package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.User;
import com.emtech.ushurusmart.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
    