package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.auth.OwnerLoginRes;
import com.emtech.ushurusmart.usermanagement.Dtos.auth.OwnerSignUp;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.KraPINValidator;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.jwtServices.JwtTokenUtil;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class OwnerAndAssistantAuthController {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private KraPINValidator kraPINValidator;

    @Autowired
    private AssistantService assistantService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestParam(name = "type", required = true) String type,
            @RequestBody OwnerSignUp data) {
        ResContructor res = new ResContructor();

        if (type.equals("owner")) {
            if (ownerService.findByEmail(data.getEmail()) != null) {
                res.setMessage(HelperUtil.capitalizeFirst(type) + " by that email! exists");
                return ResponseEntity.badRequest().body(res);
            }
            Owner owner = new Owner();
            owner.setName(data.getName());
            owner.setEmail(data.getEmail());
            owner.setPassword(data.getPassword());
            owner.setBusinessOwnerKRAPin(data.getBusinessOwnerKRAPin());
            owner.setBusinessKRAPin(data.getBusinessKRAPin());
            owner.setRole(Role.owner);
            res.setMessage(HelperUtil.capitalizeFirst(type) + " created successfully!");
            ownerService.save(owner);
            res.setData(owner);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid.");
        return ResponseEntity.badRequest().body(res);

    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@NotNull @RequestParam(name = "type", required = true) String type,
            @RequestBody LoginRequest loginReq) {
        ResContructor res = new ResContructor();

        try {
            switch (type) {
                case "owner": {
                    Owner owner = ownerService.findByEmail(loginReq.getEmail());
                    if (owner == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");

                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }

                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                                    loginReq.getPassword(),owner.getAuthorities() != null ? owner.getAuthorities() : Collections.emptyList()));


                    String token = jwtUtil.createToken(owner);
                    res.setMessage("Login successful!");




                    Map<String, Object> responseData = new HashMap<>();
                    OwnerLoginRes resData= new OwnerLoginRes();
                    resData.setName(owner.getName());
                    resData.setId(owner.getId());
                    resData.setEmail(owner.getEmail());
                    resData.setBusinessKRAPin(owner.getBusinessKRAPin());
                    resData.setBusinessOwnerKRAPin(owner.getBusinessOwnerKRAPin());
                    resData.setPhoneNumber(owner.getPhoneNumber());
                    responseData.put("owner", resData);
                    responseData.put("token", token);

                    res.setData(responseData);

                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }
                case "assistant": {
                    Assistant assistant = assistantService.findByEmail(loginReq.getEmail());
                    if (assistant == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }
                    System.out.println(assistant.toString());
                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                                    loginReq.getPassword()));
                    String token = jwtUtil.createToken(assistant);
                    res.setMessage("Login successful.");

                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put(type, assistant);
                    responseData.put("token", token);
                    res.setData(responseData);

                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }
                default:
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

            }

        } catch (BadCredentialsException e) {
            System.out.println(e.getLocalizedMessage());
            res.setMessage("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
            res.setMessage("Error  234343" + e.getLocalizedMessage());
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

}
