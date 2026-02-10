package com.nextcoder.backend.controller;

import com.nextcoder.backend.dto.LoginRequest;
import com.nextcoder.backend.dto.SignupRequest;
import com.nextcoder.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        System.out.println("Signup " + request);
        return authService.signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Login " + request);
        return authService.login(request);
    }
}