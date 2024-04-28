package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.Dtos.auth.OtpDataDto;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.RequestDtos;
import com.emtech.ushurusmart.usermanagement.factory.ResponseFactory;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.jwtServices.JwtTokenUtil;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import com.emtech.ushurusmart.utils.otp.OTPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private OwnerService ownerService;


    @Autowired
    private TransactionMiddleware transactionMiddleware;


    @Autowired
    private OTPService otpService;


    @Autowired
    private AssistantService assistantService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private Responses responses;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestParam(name = "type", required = true) String type,
            @RequestBody OwnerDto data) {
        ResContructor res = new ResContructor();

        try {
            if (type.equals("owner")) {
               return ownerService.validateAndCreateOwner(type, data, res);
            }
            res.setMessage(HelperUtil.capitalizeFirst(type) + " is an invalid type!");
            return ResponseEntity.badRequest().body(res);

        }catch (Exception e){
            return responses.create500Response(e);
        }

    }


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@NotNull @RequestParam(name = "type", required = true) String type,
            @RequestBody LoginRequest loginReq) {
        ResContructor res = new ResContructor();


        try {
            switch (type) {
                case "owner": {
                    return ownerService.loginOwner(type, loginReq, res);

                }
                case "assistant": {

                    Assistant assistant = assistantService.findByEmail(loginReq.getEmail());

                    if (assistant == null) {
                        res.setMessage("No " + HelperUtil.capitalizeFirst(type) + " by that email exists.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
                    }
                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                                    loginReq.getPassword(),assistant.getAuthorities() != null ? assistant.getAuthorities() : Collections.emptyList()));
                    otpService.sendOTP(assistant.getPhoneNumber());
                    res.setMessage("Sent an otp short code to your phone for verification");
                    Map<String,String> resBody= new HashMap<>();
                    resBody.put("type", type);
                    resBody.put("phoneNumber", assistant.getPhoneNumber());
                    res.setData(resBody);
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }
                default:
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is and invalid type!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

            }

        } catch (Exception e) {
          return responses.create500Response(e);
        }
    }

    @NotNull



    @PostMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDataDto req) {
        ResContructor res = new ResContructor();
        try {
            if(!otpService.verifyOTP(req.getPhoneNumber(),req.getOtpCode())){
                throw new BadCredentialsException("Invalid OTP code");
            }

            switch (req.getType()){
                case "owner":{
                    Owner owner = ownerService.findByPhoneNumber(req.getPhoneNumber());
                    if(owner==null){
                        throw new BadCredentialsException("Invalid owner");
                    }
                    RequestDtos.OwnerResponse resData= ResponseFactory.createOwnerResponse(owner);
                    String token = jwtUtil.createToken(owner);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("owner", resData);
                    responseData.put("token", token);
                    res.setData(responseData);
                    res.setMessage("Login successful!");
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }

                case "assistant":{
                    System.out.println(req);
                    Assistant assistant = assistantService.findByPhoneNumber(req.getPhoneNumber());
                    if(assistant==null){
                        throw new BadCredentialsException("Invalid assistant");
                    }



                    String token = jwtUtil.createToken(assistant);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("assistant", assistant);
                    responseData.put("token", token);
                    res.setData(responseData);
                    res.setMessage("Login successful!");
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);


                }
                default:
                    res.setMessage("Invalid type!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);


            }

        } catch (BadCredentialsException e) {
            res.setMessage("Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
           return responses.create500Response(e);
        }
    }

}
