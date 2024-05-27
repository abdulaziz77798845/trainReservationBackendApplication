package com.example.demo.authentication;

import com.example.demo.dto.UserDTO;
import com.example.demo.jwt.JwtService;
import com.example.demo.role_and_permissions.Permissions;
import com.example.demo.role_and_permissions.Role;
import com.example.demo.service.user_service.UserService;
import com.example.demo.user.User;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterPassenger() {
        UserDTO userDTO = new UserDTO("virat", "virat18", "password");
        User user = new User();
        user.setName("virat");
        user.setPassword("password");
        user.setRole(Role.PASSENGER);
        user.setUsername("virat18");

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class), anyMap())).thenReturn("token");

        AuthenticationResponse response = authenticationService.registerPassenger(userDTO);

        assertEquals("token", response.getJwt());
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testRegisterAdmin() {
        UserDTO userDTO = new UserDTO("virat", "virat18", "adminPassword");
        User user = new User();
        user.setName("virat");
        user.setPassword("adminpassword");
        user.setRole(Role.ADMIN);
        user.setUsername("virat18");

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class), anyMap())).thenReturn("token");

        AuthenticationResponse response = authenticationService.registerAdmin(userDTO);

        assertEquals("token", response.getJwt());
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testLogin() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("virat18", "password");
        User user = new User();
        user.setName("virat");
        user.setPassword("password");
        user.setRole(Role.PASSENGER);
        user.setUsername("virat18");


        when(userService.findByUserName(authenticationRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class), anyMap())).thenReturn("token");

        AuthenticationResponse response = authenticationService.login(authenticationRequest);

        assertEquals("token", response.getJwt());
        verify(authenticationManager, times(1)).authenticate(any());
    }
}