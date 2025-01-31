package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.LoginRequestDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        // Create new user
        User user = new User();
        user.setEmail(signupRequestDto.getEmail());

        // Hash password
        String hashedPassword = BCrypt.hashpw(signupRequestDto.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setUserType(signupRequestDto.getUserType());

//        user.setGender(User.Gender.valueOf(signupRequestDto.getGender().toUpperCase()));
//        user.setWeight(signupRequestDto.getWeight());
//        user.setHeight(signupRequestDto.getHeight());
//        user.setTargetedCarbs(signupRequestDto.getTargetedCarbs());
//        user.setTargetedProtein(signupRequestDto.getTargetedProtein());
//        user.setTargetedCalories(signupRequestDto.getTargetedCalories());

        // Save user
        userDao.saveUser(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDto) {
        Optional<User> userOptional = userDao.getUserByEmail(loginRequestDto.getEmail());

        if (userOptional.isEmpty()) {
             return new AuthResponseDTO("Invalid email or password");
        }

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(new AuthResponseDTO("Invalid email or password"));

        User user = userOptional.get();

        // Check password
        if (!BCrypt.checkpw(loginRequestDto.getPassword(), user.getPassword())) {
            return new AuthResponseDTO("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }

    public String logout(String token) {
        return "User logged out successfully.";
    }
}
