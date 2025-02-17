package com.malemate.demo.controller;

import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDto) {
        log.info("Signup request received for email: {}", signupRequestDto.getEmail());
        AuthResponseDTO response = authService.signup(signupRequestDto);
        log.info("Signup successful for email: {}", signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {
        log.info("Login request received for email: {}", loginRequestDto.getEmail());
        AuthResponseDTO response = authService.login(loginRequestDto);
        log.info("Login successful for email: {}", loginRequestDto.getEmail());
        return ResponseEntity.ok(response);
    }

}
