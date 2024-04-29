package com.emtech.ushurusmart.usermanagement.service;

import com.emtech.ushurusmart.etims_middleware.EtimsMiddleware;
import com.emtech.ushurusmart.usermanagement.Dtos.LoginRequest;
import com.emtech.ushurusmart.usermanagement.Dtos.OwnerDto;
import com.emtech.ushurusmart.usermanagement.controller.Utils;
import com.emtech.ushurusmart.usermanagement.model.Owner;
import com.emtech.ushurusmart.usermanagement.model.Role;
import com.emtech.ushurusmart.usermanagement.repository.OwnerRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OwnerServiceTest {

    @Mock
    private OwnerRepository userRepository;

    @Mock
    private EtimsMiddleware etimsMiddleware;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OTPService otpService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private OwnerService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateAndCreateUser_EmailAlreadyExists() {
        String email = "test@example.com";
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setEmail(email);
        ResContructor res = new ResContructor();

        when(userRepository.findByEmail(email)).thenReturn(new Owner());

        ResponseEntity<ResContructor> response = userService.validateAndCreateOwner("owner", ownerDto, res);
        verify(userRepository, never()).save(any(Owner.class));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getMessage().contains("Owner with that email exists!"));
    }

    @Test
    void testValidateAndCreateUser_BusinessKRAPinVerificationFails() {
        String email = "test@example.com";
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setEmail(email);
        ResContructor res = new ResContructor();

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(etimsMiddleware.verifyBusinessKRAPin(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<ResContructor> response = userService.validateAndCreateOwner("owner", ownerDto, res);
        verify(userRepository, never()).save(any(Owner.class));
        assertEquals(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getMessage()
                .contains("Owner can not be onboarded. Business is not registered by KRA!"));
    }

    @Test
    void testValidateAndCreateUser_BusinessKRAPinVerificationSucceeds() {
        String email = "test@example.com";
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setEmail(email);
        ResContructor res = new ResContructor();
        Owner owner = new Owner();

        when(userRepository.findByEmail(email)).thenReturn(null);
        when(etimsMiddleware.verifyBusinessKRAPin(any())).thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());
        when(passwordEncoder.encode(any())).thenReturn("test");
        when(userRepository.save(any())).thenReturn(owner);

        ResponseEntity<ResContructor> response = userService.validateAndCreateOwner("owner", ownerDto, res);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userRepository, times(1)).save(any(Owner.class));
        assertTrue(Objects.requireNonNull(response.getBody()).getMessage().contains("Owner created successfully!"));
    }

    @Test
    void testLoginOwner_Success() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String type = "owner";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();
        Owner owner = new Owner();
        owner.setEmail(email);
        owner.setPhoneNumber("1234567890");
        owner.setRole(Role.owner);

        when(userService.findByEmail(email)).thenReturn(owner);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList()));


        ResponseEntity<ResContructor> response = userService.loginOwner(type, loginReq, res);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("A short code has been sent to your phone for verification", res.getMessage());
        String dataString= Objects.requireNonNull(response.getBody()).getData().toString().replaceAll("(\\w+)=(\\w+)", "\"$1\":\"$2\"");
        BodyData data= Utils.parseJsonString(dataString,BodyData.class);
        assert data != null;
        assertEquals(data.getType(), "owner");
        assertEquals(data.getPhoneNumber(), "1234567890");
    }

    @Test
    void testLoginOwner_OwnerNotFound() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String type = "owner";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();

        when(userService.findByEmail(email)).thenReturn(null);

        ResponseEntity<ResContructor> response = userService.loginOwner(type, loginReq, res);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password.", res.getMessage());
    }

    @Test
    void testLoginOwner_WrongPassword() throws Exception {
        String email = "test@example.com";
        String password = "wrongPassword";
        String type = "owner";
        LoginRequest loginReq = new LoginRequest(email, password);
        ResContructor res = new ResContructor();
        Owner owner = new Owner();
        owner.setEmail(email);
        owner.setPhoneNumber("1234567890");
        owner.setRole(Role.owner);

        when(userService.findByEmail(email)).thenReturn(owner);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid email or password."));

        ResponseEntity<ResContructor> response = userService.loginOwner(type, loginReq, res);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password.", res.getMessage());
    }





    @Data
    public static class BodyData {
        private String type;
        private String phoneNumber;
    }
}
