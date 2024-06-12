package com.emtech.ushurusmart.usermanagement.controller;

import com.emtech.ushurusmart.etims_middleware.TransactionMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.Dtos.auth.OtpDataDto;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.RequestDtos;
import com.emtech.ushurusmart.usermanagement.Dtos.controller.ResetPasswordRequest;
import com.emtech.ushurusmart.usermanagement.factory.ResponseFactory;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.service.AssistantService;
import com.emtech.ushurusmart.usermanagement.service.OwnerService;
import com.emtech.ushurusmart.usermanagement.service.jwtServices.JwtTokenUtil;
import com.emtech.ushurusmart.usermanagement.service.resetpassword.ResetPasswordService;
import com.emtech.ushurusmart.utils.AuthUtils;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.controller.Responses;
import com.emtech.ushurusmart.utils.otp.OTPService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    private Responses responses;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@RequestParam(name = "type", required = true) String type, @Valid
                                    @RequestBody OwnerDto data) {
        ResContructor res = new ResContructor();


        try {
            if (type.equals("owner")) {
                return ownerService.validateAndCreateOwner(type, data, res);
            }
            res.setMessage(HelperUtil.capitalizeFirst(type) + " is an invalid type!");
            return ResponseEntity.badRequest().body(res);

        } catch (Exception e) {
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
                    return assistantService.loginAssistant(loginReq, res);
                }
                default:
                    res.setMessage(HelperUtil.capitalizeFirst(type) + " is and invalid type!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

            }

        } catch (BadCredentialsException e) {
            res.setData("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
            return responses.create500Response(e);
        }
    }




    @PostMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDataDto req) {
        ResContructor res = new ResContructor();
        try {

            String errMessage= "Invalid OTP!.";

            if (!otpService.verifyOTP(req.getPhoneNumber(), req.getOtpCode())) {
                throw new BadCredentialsException(errMessage);
            }


            switch (req.getType()) {
                case "owner": {
                    Owner owner = ownerService.findByPhoneNumber(req.getPhoneNumber());
                    if (owner == null) {
                        res.setMessage(errMessage);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
                    }
                    RequestDtos.UserResponse resData = ResponseFactory.createOwnerResponse(owner);
                    String token = jwtUtil.createToken(owner);
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("user", resData);
                    responseData.put("token", token);
                    res.setData(responseData);
                    res.setMessage("Login successful!");
                    return ResponseEntity.status(HttpStatus.CREATED).body(res);
                }

                case "assistant": {
                    Assistant assistant = assistantService.findByPhoneNumber(req.getPhoneNumber());


                    if (assistant == null) {
                        res.setMessage(errMessage);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
                    }


                    String token = jwtUtil.createToken(assistant);
                    Map<String, Object> responseData = new HashMap<>();
                    RequestDtos.UserResponse resData = ResponseFactory.createAssistantResponse(assistant);
                    responseData.put("user", resData);
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
            res.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
        } catch (Exception e) {
            return responses.create500Response(e);
        }
    }


}
