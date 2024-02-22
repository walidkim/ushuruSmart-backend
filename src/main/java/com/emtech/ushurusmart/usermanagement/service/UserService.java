package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.etrModule.service.etimsRequests;
import com.emtech.ushurusmart.usermanagement.model.Users;
import com.emtech.ushurusmart.usermanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    etimsRequests etimsRequests;

    public String userLogin(Users user,String username ,String password) {
        user = userRepository.findByUsername(user.getUsername());
        if (user == null) {
            return "Invalid username or password";
        }
        if(!user.getUsername().equals(username)){
            return "Invalid username or password";
        }
        if (!user.getPassword().equals(password)) {
            return "Invalid username or password";
        }
        return "Login successful";
    }
     
   }
    