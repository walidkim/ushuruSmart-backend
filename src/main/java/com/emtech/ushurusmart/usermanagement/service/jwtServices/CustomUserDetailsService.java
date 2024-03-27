package com.emtech.ushurusmart.usermanagement.service.jwtServices;


import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private AssistantRepository assistantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Owner owner = ownerRepository.findByEmail(email);
        if (owner != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(owner.getEmail())
                    .password(owner.getPassword())
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