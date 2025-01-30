package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    // Get user profile by userId
    public UserProfileDTO getUserProfile(int userId) {
        Optional<User> userOptional = userDao.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        return mapToUserProfileDTO(user);  // Map User entity to UserProfileDTO
    }

    //  Update user profile
    public User updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        User user = userDao.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update only the fields that are provided in the request
        if (userProfileDTO.getEmail() != null) {
            user.setEmail(userProfileDTO.getEmail());
        }
        if (userProfileDTO.getWeight() != 0) {  // Check if weight is provided
            user.setWeight(userProfileDTO.getWeight());
        }
        if (userProfileDTO.getHeight() != 0) {  // Check if height is provided
            user.setHeight(userProfileDTO.getHeight());
        }
        if (userProfileDTO.getTargetedCarbs() != 0) {  // Check if targetedCarbs is provided
            user.setTargetedCarbs(userProfileDTO.getTargetedCarbs());
        }
        if (userProfileDTO.getTargetedProtein() != 0) {  // Check if targetedProtein is provided
            user.setTargetedProtein(userProfileDTO.getTargetedProtein());
        }
        if (userProfileDTO.getTargetedCalories() != 0) {  // Check if targetedCalories is provided
            user.setTargetedCalories(userProfileDTO.getTargetedCalories());
        }
        if (userProfileDTO.getUserType() != null) {  // Check if userType is provided

                user.setUserType(userProfileDTO.getUserType());

        }

        if (userProfileDTO.getGender() != null) {  // Check if gender is provided

                user.setGender(userProfileDTO.getGender());

        }

        if (userProfileDTO.getUserUrl() != null) {  // Check if userUrl is provided
            user.setUserUrl(userProfileDTO.getUserUrl());
        }

        return userDao.saveUser(user); // Save updated user
    }

    //  Delete user by userId
    public void deleteUser(int userId) {
        userDao.deleteUser(userId);  // Delete user from the database
    }

    //  Change user password
    public void changePassword(int userId, ChangePasswordDTO changePasswordDto) {
        Optional<User> userOptional = userDao.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        // Set new password
        user.setPassword(changePasswordDto.getNewPassword());
        userDao.saveUser(user);  // Save the updated user with the new password
    }

    // Helper method to map User entity to UserProfileDTO
    private UserProfileDTO mapToUserProfileDTO(User user) {
        UserProfileDTO userProfileDto = new UserProfileDTO();
        userProfileDto.setUserId(user.getUserId());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setGender(user.getGender()); // Corrected: use name() to convert enum to String
        userProfileDto.setWeight(user.getWeight());
        userProfileDto.setHeight(user.getHeight());
        userProfileDto.setTargetedCarbs(user.getTargetedCarbs());
        userProfileDto.setTargetedProtein(user.getTargetedProtein());
        userProfileDto.setTargetedCalories(user.getTargetedCalories());
        userProfileDto.setUserUrl(user.getUserUrl()); // Added userUrl
        userProfileDto.setUserType(user.getUserType()); // Added userType, using name() for enum
        return userProfileDto;
    }

}
