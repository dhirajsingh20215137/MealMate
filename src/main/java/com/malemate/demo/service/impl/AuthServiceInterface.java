package com.malemate.demo.service.impl;



import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;

public interface AuthServiceInterface {
    AuthResponseDTO signup(SignupRequestDTO signupRequestDto);
    AuthResponseDTO login(LoginRequestDTO loginRequestDto);
}

