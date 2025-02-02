package com.malemate.demo.controller;

import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponseDTO signup(@RequestBody SignupRequestDTO signupRequestDto) {
        return authService.signup(signupRequestDto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO loginRequestDto) {
        return authService.login(loginRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful. Invalidate JWT token on the client side.");
    }
}
