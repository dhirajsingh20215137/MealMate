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

    public UserProfileDTO getUserProfile(int userId) {
        Optional<User> userOptional = userDao.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        return mapToUserProfileDTO(user);
    }


    public User updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        User user = userDao.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (userProfileDTO.getEmail() != null) {
            user.setEmail(userProfileDTO.getEmail());
        }
        if (userProfileDTO.getWeight() != 0) {
            user.setWeight(userProfileDTO.getWeight());
        }
        if (userProfileDTO.getHeight() != 0) {
            user.setHeight(userProfileDTO.getHeight());
        }
        if (userProfileDTO.getTargetedCarbs() != 0) {
            user.setTargetedCarbs(userProfileDTO.getTargetedCarbs());
        }
        if (userProfileDTO.getTargetedProtein() != 0) {
            user.setTargetedProtein(userProfileDTO.getTargetedProtein());
        }
        if (userProfileDTO.getTargetedCalories() != 0) {
            user.setTargetedCalories(userProfileDTO.getTargetedCalories());
        }
        if (userProfileDTO.getUserType() != null) {

                user.setUserType(userProfileDTO.getUserType());

        }

        if (userProfileDTO.getGender() != null) {

                user.setGender(userProfileDTO.getGender());

        }

        if (userProfileDTO.getUserUrl() != null) {
            user.setUserUrl(userProfileDTO.getUserUrl());
        }

        return userDao.saveUser(user);
    }


    public void deleteUser(int userId) {
        userDao.deleteUser(userId);
    }


    public void changePassword(int userId, ChangePasswordDTO changePasswordDto) {
        Optional<User> userOptional = userDao.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        user.setPassword(changePasswordDto.getNewPassword());
        userDao.saveUser(user);
    }


    private UserProfileDTO mapToUserProfileDTO(User user) {
        UserProfileDTO userProfileDto = new UserProfileDTO();
        userProfileDto.setUserId(user.getUserId());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setWeight(user.getWeight());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setHeight(user.getHeight());
        userProfileDto.setTargetedCarbs(user.getTargetedCarbs());
        userProfileDto.setTargetedProtein(user.getTargetedProtein());
        userProfileDto.setTargetedCalories(user.getTargetedCalories());
        userProfileDto.setUserUrl(user.getUserUrl());
        userProfileDto.setUserType(user.getUserType());
        return userProfileDto;
    }

}
