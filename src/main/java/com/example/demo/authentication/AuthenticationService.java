package com.example.demo.authentication;


import com.example.demo.dto.UserDTO;
import com.example.demo.jwt.JwtService;
import com.example.demo.role_and_permissions.Role;
import com.example.demo.service.user_service.UserService;
import com.example.demo.user.User;
import com.example.demo.repo.userRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userRepository;

    public AuthenticationService(PasswordEncoder passwordEncoder,UserService userRepository,JwtService jwtService,AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse registerPassenger(UserDTO request) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PASSENGER);
        userRepository.addUser(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse registerAdmin(UserDTO request){
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        userRepository.addUser(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authToken);
        User user=userRepository.findByUserName(authenticationRequest.getUsername()).get();
        String jwt=jwtService.generateToken(user,generateExtraClaims(user));
        return new AuthenticationResponse(jwt);
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", user.getUsername());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }
}