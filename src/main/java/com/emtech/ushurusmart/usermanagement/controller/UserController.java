package com.emtech.ushurusmart.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emtech.ushurusmart.usermanagement.model.Users;
import com.emtech.ushurusmart.usermanagement.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    UserService userService;

     @PostMapping(path="/login")
    public ResponseEntity<?> userLogin(@RequestBody Users user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        String message = userService.userLogin(user,user.getUsername(), user.getPassword());
        return ResponseEntity.ok(message);
    }
}