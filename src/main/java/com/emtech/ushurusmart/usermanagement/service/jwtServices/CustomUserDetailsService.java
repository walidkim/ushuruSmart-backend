package com.emtech.ushurusmart.usermanagement.service.jwtServices;

import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AssistantRepository assistantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Owner landlord = ownerRepository.findByEmail(email);
        if (landlord != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(landlord.getEmail())
                    .password(landlord.getPassword())
                    .build();
        }

        Assistant assistant = assistantRepository.findByEmail(email);
        if (assistant != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(assistant.getEmail())
                    .password(assistant.getPassword())
                    .build();
        }
        return null;

    }
}