package com.emtech.ushurusmart.usermanagement.service.jwtServices;


import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            List<GrantedAuthority> authorities = owner.getAuthorities().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(owner.getEmail(), owner.getPassword(), authorities);
        }

        Assistant assistant = assistantRepository.findByEmail(email);
        if (assistant != null) {
            List<GrantedAuthority> authorities = assistant.getAuthorities().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(assistant.getEmail(), assistant.getPassword(), authorities);
        }
        throw new UsernameNotFoundException("User not found");

    }
}