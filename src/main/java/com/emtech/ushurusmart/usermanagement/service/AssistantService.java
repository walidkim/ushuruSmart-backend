package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.otp.OTPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AssistantService {
    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OTPService otpService;


    public Assistant findByEmail(String email) {
        return assistantRepository.findByEmail(email);
    }

    public Assistant save(Assistant assistant) {
        assistant.setPassword(passwordEncoder.encode(assistant.getPassword()));
        return assistantRepository.save(assistant);
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }


    public ResponseEntity<ResContructor> loginAssistant(@NotNull LoginRequest loginReq, ResContructor res) throws Exception {
       try{
           Assistant assistant = findByEmail(loginReq.getEmail());

           if (assistant == null) {
               throw new BadCredentialsException(" ");
           }
           Authentication authentication = authenticationManager
                   .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                           loginReq.getPassword(),assistant.getAuthorities() != null ? assistant.getAuthorities() : Collections.emptyList()));
           otpService.sendOTP(assistant.getPhoneNumber());
           res.setMessage("A short code has been sent to your phone for verification");
           Map<String,String> resBody= new HashMap<>();
           resBody.put("type", "assistant");
           resBody.put("phoneNumber", assistant.getPhoneNumber());
           res.setData(resBody);
           return ResponseEntity.status(HttpStatus.CREATED).body(res);
       }
       catch (BadCredentialsException e){
           res.setMessage("Invalid email or password.");
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
       }

    }

    public String createEmailBody(String name, String ownerName, String email, String password ) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Welcome to Ushuru Smart</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }\n" +
                "        .email-container { width: 90%; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; }\n" +
                "        .email-header { text-align: center; padding: 20px 0; background-color: #f32525; color: #ffffff; border-radius: 10px 10px 0 0; }\n" +
                "        .email-body { padding: 20px; }\n" +
                "        .email-footer { text-align: center; padding: 20px; background-color: #f32525; color: #ffffff; border-radius: 0 0 10px 10px; }\n" +
                "        .email-footer a { color: #ffffff; text-decoration: none; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"email-header\">\n" +
                "            <h1>Welcome to Ushuru Smart</h1>\n" +
                "        </div>\n" +
                "        <div class=\"email-body\">\n" +
                "            <p>Dear "+name+",</p>\n" +
                "            <p> You have been registered by "+ownerName + " to Ushuru Smart.  We are excited to have you on Ushuru Smart! To get started, please use the following  email and  password to log in to your account:</p>\n" +
                "            <p><span> Email: <span> : <span> <strong>" + email + "</strong></span></p>\n" +
                "            <p><span> Password: <span> : <span> <strong>" + password + "</strong></span></p>\n" +
                "            <p>Please ensure to keep your password confidential and do not share it with anyone.</p>\n" +
                "            <p>If you have any questions or need further assistance, please do not hesitate to contact our support team.</p>\n" +
                "            <p>Thank you for choosing our service!</p>\n" +
                "        </div>\n" +
                "        <div class=\"email-footer\">\n" +
                "            <p>&copy; "+ LocalDate.now().getYear() +" Ushuru Smart. All rights reserved.</p>\n" +
                "            <p><a href=\"https://www.yourcompany.com/support\">Support</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public Assistant findById(long assistantId) {
        Optional<Assistant> assistant = assistantRepository.findById(assistantId);
        return assistant.orElse(null);
    }
    public List<Assistant> findAll(){
        return assistantRepository.findAll();
    }

    public void deleteById(long assistantId) {
        assistantRepository.deleteById(assistantId);
    }

    public Assistant findByPhoneNumber(String phoneNumber) {
        return assistantRepository.findByPhoneNumber(phoneNumber);
    }
}
    