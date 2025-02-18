package com.malemate.demo.controller;

import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable int userId) {
        log.info("Fetching profile for user: {}", userId);
        UserProfileDTO userProfile = userService.getUserProfile(userId);
        log.info("Fetched profile for user: {}", userId);
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int userId, @RequestBody UserProfileDTO userProfileDto) {
        log.info("Updating profile for user: {}", userId);
        userService.updateUserProfile(userId, userProfileDto);
        log.info("User profile updated successfully for user: {}", userId);
        return ResponseEntity.ok("User profile updated successfully.");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        log.info("User with ID: {} deleted successfully", userId);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable int userId, @RequestBody ChangePasswordDTO changePasswordDto) {
        log.info("Changing password for user: {}", userId);
        userService.changePassword(userId, changePasswordDto);
        log.info("Password changed successfully for user: {}", userId);
        return ResponseEntity.ok("Password changed successfully.");
    }




    @PostMapping("/{userId}/photo/upload-photo")  //check
    public ResponseEntity<?> uploadProfilePhoto(
            @PathVariable int userId,
            @RequestParam("profilePhoto") MultipartFile file) {

        log.info("Received photo upload request for userId: {}", userId);

        try {
            if (file.isEmpty()) {
                log.error("Uploaded file is empty.");
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            User user = userService.uploadProfileImage(file, userId);
            return ResponseEntity.ok(user);
        } catch (IOException e) {
            log.error("IOException while uploading file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading photo.");
        } catch (Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred.");
        }
    }

}
