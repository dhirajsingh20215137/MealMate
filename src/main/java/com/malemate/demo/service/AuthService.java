package com.malemate.demo.service;

import com.malemate.demo.dao.UserDao;
import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.exceptions.BadRequestException;
import com.malemate.demo.exceptions.UnauthorizedException;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class AuthService {

    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserDao userDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO signup(SignupRequestDTO signupRequestDto) {
        log.info("Signup request received for email: {}", signupRequestDto.getEmail());
        validateSignup(signupRequestDto);
        log.info("Signup successful for email: {}", signupRequestDto.getEmail());
        Optional<User> existingUser = userDao.getUserByEmail(signupRequestDto.getEmail());
        if (existingUser.isPresent()) {
            log.error("Email already exists: {}", signupRequestDto.getEmail());
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(BCrypt.hashpw(signupRequestDto.getPassword(), BCrypt.gensalt()));
        if (Objects.nonNull(signupRequestDto.getGender())) {
            user.setGender(signupRequestDto.getGender());
        }
        if (Objects.nonNull(signupRequestDto.getWeight())) {
            user.setWeight(signupRequestDto.getWeight());
        }
        if (Objects.nonNull(signupRequestDto.getHeight())) {
            user.setHeight(signupRequestDto.getHeight());
        }
        if (Objects.nonNull(signupRequestDto.getTargetedCarbs())) {
            user.setTargetedCarbs(signupRequestDto.getTargetedCarbs());
        }
        if (Objects.nonNull(signupRequestDto.getTargetedProtein())) {
            user.setTargetedProtein(signupRequestDto.getTargetedProtein());
        }
        if (Objects.nonNull(signupRequestDto.getTargetedCalories())) {
            user.setTargetedCalories(signupRequestDto.getTargetedCalories());
        }

        userDao.saveUser(user);
        log.info("User successfully created with email: {}", signupRequestDto.getEmail());
        String token = jwtUtil.generateToken(user);
        log.info("Token generated for user with email: {}", signupRequestDto.getEmail());
        return new AuthResponseDTO(token, user);
    }


    public AuthResponseDTO login(LoginRequestDTO loginRequestDto) {
        log.info("Login attempt for email: {}", loginRequestDto.getEmail());
        validateLogin(loginRequestDto);
        Optional<User> userOptional = userDao.getUserByEmail(loginRequestDto.getEmail());
        if (userOptional.isEmpty() || !BCrypt.checkpw(loginRequestDto.getPassword(), userOptional.get().getPassword())) {
            log.error("Login failed: Invalid credentials for email: {}", loginRequestDto.getEmail());
            throw new UnauthorizedException("Invalid email or password");
        }
        User user = userOptional.get();
        log.info("User authenticated successfully for email: {}", user.getEmail());
        String token = jwtUtil.generateToken(user);
        log.info("Token generated for user with email: {}", user.getEmail());
        return new AuthResponseDTO(token, user);
    }


    private void validateSignup(SignupRequestDTO signupRequestDto) {
        if (StringUtils.isBlank(signupRequestDto.getEmail())) {
            log.error("Signup validation failed: Email is required");
            throw new BadRequestException("Email is required");
        }
        if (StringUtils.isBlank(signupRequestDto.getPassword())) {
            log.error("Signup validation failed: Password is required");
            throw new BadRequestException("Password is required");
        }
        log.info("Signup validation passed for email: {}", signupRequestDto.getEmail());
    }


    private void validateLogin(LoginRequestDTO loginRequestDto) {
        if (StringUtils.isBlank(loginRequestDto.getEmail())) {
            log.error("Login validation failed: Email is required");
            throw new BadRequestException("Email is required");
        }
        if (StringUtils.isBlank(loginRequestDto.getPassword())) {
            log.error("Login validation failed: Password is required");
            throw new BadRequestException("Password is required");
        }
        log.info("Login validation passed for email: {}", loginRequestDto.getEmail());
    }
}
