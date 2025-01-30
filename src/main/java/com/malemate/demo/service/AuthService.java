package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.AuthResponseDTO;
import com.malemate.demo.dto.SignupRequestDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseDTO signup(SignupRequestDTO signupRequestDto) {
        Optional<User> existingUser = userDao.getUserByEmail(signupRequestDto.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(signupRequestDto.getEmail());

        // Use mindrot BCrypt to hash the password
        String hashedPassword = BCrypt.hashpw(signupRequestDto.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        user.setGender(User.Gender.valueOf(signupRequestDto.getGender().toUpperCase()));
        user.setWeight(signupRequestDto.getWeight());
        user.setHeight(signupRequestDto.getHeight());
        user.setTargetedCarbs(signupRequestDto.getTargetedCarbs());
        user.setTargetedProtein(signupRequestDto.getTargetedProtein());
        user.setTargetedCalories(signupRequestDto.getTargetedCalories());

        // Save user to the database
        userDao.saveUser(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user);

        return new AuthResponseDTO(token);
    }

    // You can add a method to validate the password during login if needed
    public boolean checkPassword(String plaintextPassword, String storedHash) {
        return BCrypt.checkpw(plaintextPassword, storedHash);
    }
}
