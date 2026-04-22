package com.example.api;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.dto.CreateUserRequestDto;
import com.example.dto.CreateUserResponseDto;
import com.example.security.JwtUtil;
import com.example.service.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return userService.createUser(createUserRequestDto);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public AuthResponse loginUser(@RequestBody AuthRequest authRequest) {
        // Logic to authenticate the user

        AuthResponse authResponse = new AuthResponse();
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword());
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .map(role -> role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role)
                .toList();
        String jwtToken = jwtUtil.generateToken(username, roles);
        authResponse.setToken(jwtToken);
        authResponse.setMessage("Login successful");
        return authResponse;
    }

}
