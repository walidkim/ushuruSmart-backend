package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Assistant;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.AssistantRepository;
import com.emtech.ushurusmart.utils.controller.ResContructor;
import com.emtech.ushurusmart.utils.otp.OTPService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AssistantAuthTest {

    @Mock
    private AssistantRepository assistantRepository;

    @Mock
    private EtimsMiddleware etimsMiddleware;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OTPService otpService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AssistantService assistantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testLoginAssistant_Success() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String type = "assistant";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();
        Assistant assistant = new Assistant();
        assistant.setEmail(email);
        assistant.setPhoneNumber("1234567890");
        assistant.setRole(Role.assistant);

        when(assistantService.findByEmail(any())).thenReturn(assistant);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList()));


        ResponseEntity<ResContructor> response = assistantService.loginAssistant(loginReq, res);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("A short code has been sent to your phone for verification", res.getMessage());
        String dataString= Objects.requireNonNull(response.getBody()).getData().toString().replaceAll("(\\w+)=(\\w+)", "\"$1\":\"$2\"");
        BodyData data= Utils.parseJsonString(dataString,BodyData.class);
        assert data != null;
        assertEquals(data.getType(), "assistant");
        assertEquals(data.getPhoneNumber(), "1234567890");

    }

    @Test
    void testLoginOwner_OwnerNotFound() throws Exception {
        String email = "test@example.com";
        String password = "password";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();

        when(assistantService.findByEmail(email)).thenReturn(null);

        ResponseEntity<ResContructor> response = assistantService.loginAssistant(loginReq, res);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password.", res.getMessage());
    }
//
    @Test
    void testLoginOwner_WrongPassword() throws Exception {
        String email = "test@example.com";
        String password = "wrongPassword";
        String type = "owner";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();
        Assistant owner = new Assistant();
        owner.setEmail(email);
        owner.setPhoneNumber("1234567890");
        owner.setRole(Role.owner);

        when(assistantService.findByEmail(email)).thenReturn(owner);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid email or password."));

        ResponseEntity<ResContructor> response = assistantService.loginAssistant(loginReq, res);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password.", res.getMessage());
    }
//




    @Data
    public static class BodyData {
        private String type;
        private String phoneNumber;
    }
}
