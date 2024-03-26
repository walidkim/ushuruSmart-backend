package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.ResConstructor;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.KraPINValidator;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.jwtServices.JwtTokenUtil;
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
            @RequestBody Owner data) {
        ResConstructor res = new  ResConstructor();

        switch (type) {
            case ("owner"): {

                if (ownerService.findByEmail(data.getEmail()) != null) {
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " by that email! exists");
                    return ResponseEntity.badRequest().body(res);
                }
                Owner owner = new Owner();
                owner.setName(data.getName());
                owner.setEmail(data.getEmail());
                owner.setPassword(data.getPassword());
                owner.setBusinessOwnerKraPin(data.getBusinessOwnerKraPin());
                owner.setBusinessKraPin(data.getBusinessKraPin());
                owner.setPhoneNumber(data.getPhoneNumber());
                if (kraPINValidator.validateKRAPins(data.getBusinessKraPin(),data.getBusinessOwnerKraPin())) {
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " created successfully!");

                    ownerService.save(owner);
                    res.setData(owner);
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                } else {
                    res.setMessage("Invalid details provided.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
                }

            }
            default: {
                res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid.");
                return ResponseEntity.badRequest().body(res);
            }

        }

    }
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestParam(name = "type") String type, @RequestBody LoginRequest loginReq) {
        ResConstructor res = new ResConstructor();

        try {
            switch (type) {
                case "owner":
                    System.out.println("getting owner..");
                    Owner owner = ownerService.findByEmail(loginReq.getEmail());
                    if (owner == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " with that email exists.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }
                    System.out.println("authenticating....");
                    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
                    System.out.println(authentication.getPrincipal());
                    String token = jwtUtil.createToken(authentication.getName());
                    System.out.println("this is the token:" +token);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("owner", owner);
                    responseData.put("token", token);
                    res.setMessage("Login successful!");
                    res.setData(responseData);
                    System.out.println(res);
                    return ResponseEntity.status(HttpStatus.OK).body(res);

                case "assistant":
                    System.out.println("getting.. assistant");
                    Assistant assistant = assistantService.findByEmail(loginReq.getEmail());
                    if (assistant == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " with that email exists.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }

                    Authentication assistantAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
                    String assistantToken = jwtUtil.createToken(assistantAuthentication.getName());

                    Map<String, Object> assistantData = new HashMap<>();
                    assistantData.put("assistant", assistant);
                    assistantData.put("token", assistantToken);
                    res.setMessage("Login successful.");
                    res.setData(assistantData);

                    return ResponseEntity.status(HttpStatus.OK).body(res);

                default:
                    System.out.println("getting default");
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

        } catch (BadCredentialsException e) {
            res.setMessage("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
            res.setMessage("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

//
//    @PostMapping(value = "/login")
//    public ResponseEntity<?> login(@NotNull @RequestParam(name = "type", required = true) String type,
//            @RequestBody LoginRequest loginReq) {
//        ResContructor res = new ResContructor();
//
//        try {
//            switch (type) {
//                case "owner": {
//                    System.out.println("logged in.........................");
//                    Owner owner = ownerService.findByEmail(loginReq.getEmail());
//                    if (owner == null) {
//                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
//
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
//                    }
//                    Authentication authentication = authenticationManager
//                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
//                                    loginReq.getPassword()));
//                    String token = jwtUtil.createToken(authentication.getName());
//                    res.setMessage("Login successful!");
//
//                    Map<String, Object> responseData = new HashMap<>();
//                    responseData.put("owner", owner);
//                    responseData.put("token", token);
//                    res.setData(responseData);
//                    return ResponseEntity.status(HttpStatus.OK).body(res);
//
//                }
//                case "assistant": {
//                    Assistant assistant = assistantService.findByEmail(loginReq.getEmail());
//                    if (assistant == null) {
//                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
//                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
//                    }
//                    System.out.println(assistant.toString());
//                    Authentication authentication = authenticationManager
//                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
//                                    loginReq.getPassword()));
//                    String token = jwtUtil.createToken(authentication.getName());
//                    res.setMessage("Login successful.");
//
//                    Map<String, Object> responseData = new HashMap<>();
//                    responseData.put(type, assistant);
//                    responseData.put("token", token);
//                    res.setData(responseData);
//
//                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
//                }
//                default:
//                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is invalid");
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//
//            }
//
//        } catch (BadCredentialsException e) {
//            System.out.println(e.getLocalizedMessage());
//            res.setMessage("Invalid email or password.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
//        } catch (Exception e) {
//            res.setMessage("Error " + e.getLocalizedMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
//        }
//    }

}
