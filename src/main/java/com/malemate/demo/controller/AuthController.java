package com.malemate.demo.controller;


import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public AuthResponseDTO signup(@RequestBody SignupRequestDTO signupRequestDto) {
        return authService.signup(signupRequestDto);
    }


}
