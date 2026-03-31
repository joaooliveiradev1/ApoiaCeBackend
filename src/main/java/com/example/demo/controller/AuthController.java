package com.example.demo.controller;

import com.example.demo.models.Dto.LoginRequestDTO;
import com.example.demo.models.Dto.LoginResponseDTO;
import com.example.demo.models.Dto.UsuarioCreateRequest;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Email ou senha inválidos"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UsuarioCreateRequest request) {
        authService.registro(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
