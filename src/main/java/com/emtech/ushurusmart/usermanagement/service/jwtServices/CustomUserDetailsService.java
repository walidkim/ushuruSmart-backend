package com.emtech.ushurusmart.usermanagement.service.jwtServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.usermanagement.model.*;
import com.emtech.ushurusmart.usermanagement.repository.AdminRepository;
import com.emtech.ushurusmart.usermanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin landlord = adminRepository.findByEmail(email);
        if (landlord != null) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(landlord.getEmail())
                    .password(landlord.getPassword())
                    .build();
            return userDetails;
        }

        User tenant = userRepository.findByEmail(email);
        if (tenant != null) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(tenant.getEmail())
                    .password(tenant.getPassword())
                    .build();
            return userDetails;
        }
        return null;

    }
}