package com.malemate.demo.controller;

import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;  // Use the UserService class instead of User entity

    // ✅ Get user profile
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable int userId) {
        UserProfileDTO userProfile = userService.getUserProfile(userId);  // Get user profile using UserService
        return ResponseEntity.ok(userProfile);
    }

    // ✅ Update user profile
    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int userId, @RequestBody UserProfileDTO userProfileDto) {
        userService.updateUserProfile(userId, userProfileDto);  // Update user profile using UserService
        return ResponseEntity.ok("User profile updated successfully.");
    }

    // ✅ Delete user account
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);  // Delete user using UserService
        return ResponseEntity.ok("User deleted successfully.");
    }

    // ✅ Change password
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable int userId, @RequestBody ChangePasswordDTO changePasswordDto) {
        userService.changePassword(userId, changePasswordDto);  // Change password using UserService
        return ResponseEntity.ok("Password changed successfully.");
    }
}
