package com.example.demo.authentication;

import com.example.demo.dto.UserDTO;
import com.example.demo.role_and_permissions.Role;
import com.example.demo.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterPassenger(){
        UserDTO userDTO = new UserDTO();

        AuthenticationResponse expectedResponse = new AuthenticationResponse("jwttoken");

        when(authenticationService.registerPassenger(userDTO)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerPassenger(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testRegisterAdmin(){
        UserDTO userDTO = new UserDTO();

        AuthenticationResponse expectedResponse = new AuthenticationResponse("jwttoken");
        when(authenticationService.registerAdmin(userDTO)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerAdmin(userDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}