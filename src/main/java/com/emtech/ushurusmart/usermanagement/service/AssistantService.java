package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {
    @Autowired
    private AssistantRepository assistantRepository;


    public Assistant findByEmail(String email) {
        return assistantRepository.findByEmail(email);
    }
}
    