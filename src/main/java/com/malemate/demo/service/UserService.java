package com.malemate.demo.service;

import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.exceptions.BadRequestException;
import com.malemate.demo.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@Log4j2
@Service
public class UserService {

    private final UserDao userDao;

//    private static final String UPLOAD_DIR = "uploads/";  //
private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";



    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public UserProfileDTO getUserProfile(int userId) {
        log.info("Fetching user profile for userId: {}", userId);

        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> {
                    log.error("User not found or marked as deleted for userId: {}", userId);
                    return new ResourceNotFoundException("User not found or marked as deleted");
                });

        return mapToUserProfileDTO(user);
    }

    // ✅ 2. Update User Profile
    public void updateUserProfile(int userId, UserProfileDTO userProfileDTO) {
        log.info("Updating profile for userId: {}", userId);
        validateUserProfileDTO(userProfileDTO);

        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found or marked as deleted"));

        if (!Objects.isNull(userProfileDTO.getWeight()) && userProfileDTO.getWeight() > 0) {
            user.setWeight(userProfileDTO.getWeight());
        }
        if (!Objects.isNull(userProfileDTO.getHeight()) && userProfileDTO.getHeight() > 0) {
            user.setHeight(userProfileDTO.getHeight());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedCarbs()) && userProfileDTO.getTargetedCarbs() > 0) {
            user.setTargetedCarbs(userProfileDTO.getTargetedCarbs());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedProtein()) && userProfileDTO.getTargetedProtein() > 0) {
            user.setTargetedProtein(userProfileDTO.getTargetedProtein());
        }
        if (!Objects.isNull(userProfileDTO.getTargetedCalories()) && userProfileDTO.getTargetedCalories() > 0) {
            user.setTargetedCalories(userProfileDTO.getTargetedCalories());
        }
        if (userProfileDTO.getGender() != null) {
            user.setGender(userProfileDTO.getGender());
        }
        if (StringUtils.isNotBlank(userProfileDTO.getUserUrl())) {
            user.setUserUrl(userProfileDTO.getUserUrl());
        }

        userDao.saveUser(user);
        log.info("User profile updated successfully for userId: {}", userId);
    }


    public User uploadProfileImage(MultipartFile file, int userId) throws IOException {
        log.info("Uploading profile image for userId: {}", userId);

        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found or marked as deleted"));


        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String uniqueFilename = baseName + "_" + System.currentTimeMillis() + fileExtension;

        Path filePath = uploadPath.resolve(uniqueFilename);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }

        user.setUserUrl(uniqueFilename);
        userDao.saveUser(user);

        log.info("Profile photo uploaded successfully for userId: {}", userId);
        return user;
    }




    // ✅ 4. Delete User
    public void deleteUser(int userId) {
        log.info("Deleting user with userId: {}", userId);

        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found or marked as deleted"));

        user.setDeleted(true);
        userDao.saveUser(user);

        log.info("User successfully deleted (soft delete) with userId: {}", userId);
    }

    // ✅ 5. Change Password
    public void changePassword(int userId, ChangePasswordDTO changePasswordDto) {
        log.info("Changing password for userId: {}", userId);

        validatePasswordChangeRequest(changePasswordDto);

        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found or marked as deleted"));

        user.setPassword(changePasswordDto.getNewPassword());
        userDao.saveUser(user);

        log.info("Password changed successfully for userId: {}", userId);
    }

    // ✅ Helper Methods
    private UserProfileDTO mapToUserProfileDTO(User user) {
        UserProfileDTO userProfileDto = new UserProfileDTO();
        userProfileDto.setUserId(user.getUserId());
        userProfileDto.setWeight(user.getWeight());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setHeight(user.getHeight());
        userProfileDto.setTargetedCarbs(user.getTargetedCarbs());
        userProfileDto.setTargetedProtein(user.getTargetedProtein());
        userProfileDto.setTargetedCalories(user.getTargetedCalories());
        userProfileDto.setUserUrl(user.getUserUrl());
        return userProfileDto;
    }

    private void validateUserProfileDTO(UserProfileDTO userProfileDTO) {
        if (userProfileDTO == null) {
            log.error("UserProfileDTO is null");
            throw new BadRequestException("User profile data cannot be null");
        }
    }

    private void validatePasswordChangeRequest(ChangePasswordDTO changePasswordDto) {
        if (changePasswordDto == null) {
            log.error("ChangePasswordDTO is null");
            throw new BadRequestException("Password change request cannot be null");
        }
        if (StringUtils.isBlank(changePasswordDto.getNewPassword()) || changePasswordDto.getNewPassword().length() < 6) {
            log.error("Invalid password provided");
            throw new BadRequestException("Password must be at least 6 characters long");
        }
    }
}
