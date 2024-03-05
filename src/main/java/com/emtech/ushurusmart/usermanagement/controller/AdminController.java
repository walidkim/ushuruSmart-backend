package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.SignUpReq;
import com.emtech.ushurusmart.usermanagement.model.Admin;
import com.emtech.ushurusmart.usermanagement.model.User;
import com.emtech.ushurusmart.usermanagement.service.AdminService;
import com.emtech.ushurusmart.usermanagement.service.UserService;
import com.emtech.ushurusmart.usermanagement.service.jwtServices.JwtTokenUtil;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestParam(name = "type", required = true) String type,
            @RequestBody SignUpReq data) {
        switch (type) {
            case ("admin"): {

                if (adminService.findByEmail(data.getEmail()) != null) {
                    return ResponseEntity.badRequest().body(HelperUtil.capitalizeFirst(type)+ " with that email exists!");
                }
                Admin landlord = new Admin();
                landlord.setName(data.getName());
                landlord.setEmail(data.getEmail());
                landlord.setPassword(data.getPassword());

                adminService.save(landlord);
                System.out.println("Done of writing");
                return ResponseEntity.status(HttpStatus.CREATED).body(HelperUtil.capitalizeFirst(type)+ " created successfully!");
            }
            default: {
                return ResponseEntity.badRequest().body(HelperUtil.capitalizeFirst(type)+ " is invalid.");
            }

        }



    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@NotNull @RequestParam(name = "type", required = true) String type,@RequestBody LoginRequest loginReq) {

        try {
            switch (type){
                case "admin":{
                    Admin landLord = adminService.findByEmail(loginReq.getEmail());
                    if (landLord == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin by that email exists.");
                    }
                    break;
                }
                case "tenant":{
                    User tenant = userService.findByEmail(loginReq.getEmail());
                    if (tenant == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tenant by that email exists.");
                    }
                    break;
                }
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
//
            }
//            System.out.println("logging in");
//            mongoDBService.createGroceryItems();


            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String token = jwtUtil.createToken(authentication.getName());

            return ResponseEntity.status(HttpStatus.CREATED).body(token);

        } catch (BadCredentialsException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error "+ e.getLocalizedMessage());
        }
    }
}
