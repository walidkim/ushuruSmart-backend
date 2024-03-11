package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.ResContructor;
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

import java.util.HashMap;
import java.util.Map;

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
        ResContructor res= new ResContructor();

        switch (type) {
            case ("owner"): {

                if (ownerService.findByEmail(data.getEmail()) != null) {
                    res.setMessage(HelperUtil.capitalizeFirst(type)+ " by that email! exists");
                    return ResponseEntity.badRequest().body(res);
                }
                Owner owner = new Owner();
                owner.setName(data.getName());
                owner.setEmail(data.getEmail());
                owner.setPassword(data.getPassword());
                res.setMessage(HelperUtil.capitalizeFirst(type)+ " created successfully!");
                ownerService.save(owner);
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
            }
            default: {
                res.setMessage(HelperUtil.capitalizeFirst(type)+ " is invalid.");
                return ResponseEntity.badRequest().body(res);
            }

        }



    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@NotNull @RequestParam(name = "type", required = true) String type,@RequestBody LoginRequest loginReq) {
        ResContructor res= new ResContructor();

        try {
            switch (type){
                case "owner":{
                    Owner owner = ownerService.findByEmail(loginReq.getEmail());
                    if (owner == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) +" by that email exists.");

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }
                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
                    String token = jwtUtil.createToken(authentication.getName());
                    res.setMessage("Login successful!");

                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("owner", owner);
                    responseData.put("token", token);
                    res.setData(responseData);

                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }
                case "assistant":{
                     Assistant assistant = assistantService.findByEmail(loginReq.getEmail());
                    if (assistant == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }
                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
                    String token = jwtUtil.createToken(authentication.getName());
                    res.setMessage("Login successful.");

                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put(type+ "_details", assistant);
                    responseData.put("token", token);
                    res.setData(responseData);

                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }
                default:
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

            }




        } catch (BadCredentialsException e) {
            res.setMessage("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
            res.setMessage("Error "+ e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
