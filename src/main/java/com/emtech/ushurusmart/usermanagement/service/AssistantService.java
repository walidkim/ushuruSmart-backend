package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssistantService {
    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Assistant findByEmail(String email) {
        return assistantRepository.findByEmail(email);
    }

    public Assistant save(Assistant assistant) {
        assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
        return assistantRepository.save(assistant);
    }

    public Assistant findById(long assistantId) {
        Optional<Assistant> assistant = assistantRepository.findById(assistantId);
        return assistant.orElse(null);
    }
    public List<Assistant> findAll(){
        return assistantRepository.findAll();
    }

    public void deleteById(long assistantId) {
        assistantRepository.deleteById(assistantId);
    }

    public Assistant findByPhoneNumber(String phoneNumber) {
        return assistantRepository.findByPhoneNumber(phoneNumber);
    }
}
    