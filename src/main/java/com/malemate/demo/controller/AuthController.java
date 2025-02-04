package com.malemate.demo.controller;

import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.service.AuthService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDto)
    {
        log.info("Signup request received for email: " + signupRequestDto.getEmail());
        AuthResponseDTO response = authService.signup(signupRequestDto);
        log.info("Signup successful for email: " + signupRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {
        log.info("Login attempt for email: " + loginRequestDto.getEmail());
        AuthResponseDTO response = authService.login(loginRequestDto);
        log.info("Login successful for email: " + loginRequestDto.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        log.info("Logout request received");
        log.info("Logout successful");
        return ResponseEntity.ok("Logout successful. Invalidate JWT token on the client side.");
    }
}
