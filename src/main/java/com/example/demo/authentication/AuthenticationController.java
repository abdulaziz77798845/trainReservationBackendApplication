package com.example.demo.authentication;


import com.example.demo.dto.UserDTO;
import com.example.demo.user.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private  AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerPassenger(@RequestBody UserDTO request){
        try {
            return ResponseEntity.ok(authenticationService.registerPassenger(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse("REGISTER FAILED "+e.getMessage()));
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody UserDTO request){
        try {
            return ResponseEntity.ok(authenticationService.registerAdmin(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse("REGISTER FAILED"+e.getMessage()));
        }
    }

    @Operation(summary = "add admins", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add/admin")
    public ResponseEntity<AuthenticationResponse> addAdmin(@RequestBody UserDTO userDTO){
        try {
            AuthenticationResponse response = authenticationService.registerAdmin(userDTO);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse("ADMIN ADDITION FAILED"+e.getMessage()));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("AUTHENTICATE FAILED "+e.getMessage() ));
        }
    }
}