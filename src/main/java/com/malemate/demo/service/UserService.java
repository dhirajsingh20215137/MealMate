package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserProfileDTO getUserProfile(int userId) {
        log.info("Fetching user profile for userId: {}", userId);

        User user = userDao.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        return mapToUserProfileDTO(user);
    }

    public User updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        log.info("Updating profile for userId: {}", userId);

        validateUserProfileDTO(userProfileDTO);

        User user = userDao.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for userId: {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        if (StringUtils.isNotBlank(userProfileDTO.getEmail())) {
            user.setEmail(userProfileDTO.getEmail());
        }
        if (!Objects.isNull(userProfileDTO.getWeight()) && userProfileDTO.getWeight() > 0) {
            user.setWeight(userProfileDTO.getWeight());
        }
        if (!Objects.isNull(userProfileDTO.getHeight()) && userProfileDTO.getHeight() > 0) {
            user.setHeight(userProfileDTO.getHeight());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedCarbs() ) && userProfileDTO.getTargetedCarbs() > 0) {
            user.setTargetedCarbs(userProfileDTO.getTargetedCarbs());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedProtein() ) && userProfileDTO.getTargetedProtein() > 0) {
            user.setTargetedProtein(userProfileDTO.getTargetedProtein());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedCalories())  && userProfileDTO.getTargetedCalories() > 0) {
            user.setTargetedCalories(userProfileDTO.getTargetedCalories());
        }
        if (userProfileDTO.getUserType() != null) {
            user.setUserType(userProfileDTO.getUserType());
        }
        if (userProfileDTO.getGender() != null) {
            user.setGender(userProfileDTO.getGender());
        }
        if (StringUtils.isNotBlank(userProfileDTO.getUserUrl())) {
            user.setUserUrl(userProfileDTO.getUserUrl());
        }

        userDao.saveUser(user);
        log.info("User profile updated successfully for userId: {}", userId);
        return user;
    }

    public void deleteUser(int userId) {
        log.info("Deleting user with userId: {}", userId);

        if (!userDao.getUserById(userId).isPresent()) {
            log.error("User not found for deletion with userId: {}", userId);
            throw new IllegalArgumentException("User not found");
        }

        userDao.deleteUser(userId);
        log.info("User successfully deleted with userId: {}", userId);
    }

    public void changePassword(int userId, ChangePasswordDTO changePasswordDto) {
        log.info("Changing password for userId: {}", userId);

        validatePasswordChangeRequest(changePasswordDto);

        User user = userDao.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for password change, userId: {}", userId);
                    return new IllegalArgumentException("User not found");
                });

        user.setPassword(changePasswordDto.getNewPassword());
        userDao.saveUser(user);

        log.info("Password changed successfully for userId: {}", userId);
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


    private void validateUserProfileDTO(UserProfileDTO userProfileDTO) {
        if (userProfileDTO == null) {
            log.error("UserProfileDTO is null");
            throw new IllegalArgumentException("User profile data cannot be null");
        }
        if (userProfileDTO.getEmail() != null && !userProfileDTO.getEmail().contains("@")) {
            log.error("Invalid email provided: {}", userProfileDTO.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePasswordChangeRequest(ChangePasswordDTO changePasswordDto) {
        if (changePasswordDto == null) {
            log.error("ChangePasswordDTO is null");
            throw new IllegalArgumentException("Password change request cannot be null");
        }
        if (StringUtils.isBlank(changePasswordDto.getNewPassword()) || changePasswordDto.getNewPassword().length() < 6) {
            log.error("Invalid password provided");
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }
}
