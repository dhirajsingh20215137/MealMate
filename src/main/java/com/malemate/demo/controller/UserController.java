package com.malemate.demo.controller;

import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
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
}
