package com.emtech.ushurusmart.usermanagement.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.usermanagement.model.Admin;
import com.emtech.ushurusmart.usermanagement.model.Users;
import com.emtech.ushurusmart.usermanagement.repository.AdminRepository;
import com.emtech.ushurusmart.usermanagement.repository.UserRepository;
import com.emtech.ushurusmart.usermanagement.service.AdminService;
import com.emtech.ushurusmart.usermanagement.service.CreateUSerService;

@RestController
@RequestMapping(path="/admin")
public class AdminController {
    @Autowired
    public AdminService adminService;
    @Autowired
    public CreateUSerService createUser;
    @Autowired
    public AdminRepository adminRepository;
    @Autowired
    public UserRepository userRepository;

    @PostMapping(path="/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        String message = adminService.adminLogin(admin,admin.getUsername(), admin.getPassword());
        return ResponseEntity.ok(message);
    }
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser (@RequestBody Users user, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        if (userRepository.findByUsername(user.getUsername()) != null){
            return ResponseEntity.badRequest().body("Username already exists!");
        }
        createUser.saveUser(user);
        return ResponseEntity.ok("User created Successfully");
    }
    @PutMapping("/updateUser")
    public ResponseEntity<String> updateUser (@RequestBody Users user, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        if (userRepository.findByUsername(user.getUsername()) == null){
            return ResponseEntity.badRequest().body("User not found");
        }
        createUser.updateUser(null,user);
        return ResponseEntity.ok("User updated successfully");
    }
    @GetMapping("/allUsers")
    public ResponseEntity<Iterable<Users>> getAllUsers(){
        return ResponseEntity.ok(createUser.getAllUsers());
    }
    @GetMapping("/users/branch/{branch}")
    public ResponseEntity<Iterable<String>> getUsersByBranch(@PathVariable String branch){
        return ResponseEntity.ok(createUser.getUserByBranch(branch));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        createUser.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
       
    }


    
}