package com.emtech.ushurusmart.usermanagement.service.jwtServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Utils {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String setPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
