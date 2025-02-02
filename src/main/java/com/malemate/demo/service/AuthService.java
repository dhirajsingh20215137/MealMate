package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserDao userDao, JwtUtil jwtUtil) {
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO signup(SignupRequestDTO signupRequestDto) {
        Optional<User> existingUser = userDao.getUserByEmail(signupRequestDto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(signupRequestDto.getEmail());
        user.setPassword(BCrypt.hashpw(signupRequestDto.getPassword(), BCrypt.gensalt()));
        user.setUserType(signupRequestDto.getUserType());

        userDao.saveUser(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDto) {
        Optional<User> userOptional = userDao.getUserByEmail(loginRequestDto.getEmail());

        if (userOptional.isEmpty() || !BCrypt.checkpw(loginRequestDto.getPassword(), userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();
        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }
}
