package com.e_aganda.demo.controller;

import com.e_aganda.demo.dto.AuthResponse;
import com.e_aganda.demo.dto.LoginRequest;
import com.e_aganda.demo.dto.RegisterRequest;
import com.e_aganda.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins ="http://localhost:4200")
public class AuthController {
    private final AuthService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
