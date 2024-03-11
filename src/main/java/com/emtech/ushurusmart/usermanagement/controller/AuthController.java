package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.SignUpReq;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
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
public class AuthController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestParam(name = "type", required = true) String type,
            @RequestBody SignUpReq data) {
        switch (type) {
            case ("owner"): {

                if (ownerService.findByEmail(data.getEmail()) != null) {
                    return ResponseEntity.badRequest().body(HelperUtil.capitalizeFirst(type)+ " with that email exists!");
                }
                Owner owner = new Owner();
                owner.setName(data.getName());
                owner.setEmail(data.getEmail());
                owner.setPassword(data.getPassword());

                ownerService.save(owner);
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
                case "owner":{
                    Owner owner = ownerService.findByEmail(loginReq.getEmail());
                    if (owner == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No " + HelperUtil.capitalizeFirst(type) +" by that email exists.");
                    }
                    break;
                }
                case "assistant":{
                     Assistant assistant = assistantService.findByEmail(loginReq.getEmail());
                    if (assistant == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
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
