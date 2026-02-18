package dev.marco.taskapi.web.controller;

import dev.marco.taskapi.domain.service.AuthService;
import dev.marco.taskapi.web.dto.LoginRequest;
import dev.marco.taskapi.web.dto.LoginResponse;
import dev.marco.taskapi.web.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // IMPORTANTE: Usar Spring, no Swagger
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(
                request.username(), 
                request.email(), 
                request.password()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new LoginResponse(token, request.username()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                                 .body(Map.of("error", "Error al registrar: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticate(request.username(), request.password());
            return ResponseEntity.ok(new LoginResponse(token, request.username()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Credenciales incorrectas"));
        }
    }
}