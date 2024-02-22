package com.emtech.ushurusmart.usermanagement.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emtech.ushurusmart.usermanagement.model.Users;
import com.emtech.ushurusmart.usermanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CreateUSerService {
    private UserRepository userRepository;

    @Autowired
    public Users user;

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Transactional
    public Iterable<Users> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public Iterable<String> getUserByBranch(String branch){
        return userRepository.findByBranch(branch).stream().map(Users::getUsername).collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Transactional
    public void saveUser(Users user) {
        userRepository.save(user);
    }

    @Transactional
    public Users updateUser(Long  id, Users updatedUser){
        Optional<Users> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            Users user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setPhonenumber(updatedUser.getPhonenumber());
            user.setBranch(updatedUser.getBranch());
            return userRepository.save(user);
        }
        return null;
        
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
