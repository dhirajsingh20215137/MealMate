package com.malemate.demo.controller;

import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.service.AuthService;
import com.malemate.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

//    @Autowired
//    private UserService userService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable int userId) {
        UserProfileDTO userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }


    @PostMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int userId, @RequestBody UserProfileDTO userProfileDto) {
        userService.updateUserProfile(userId, userProfileDto);
        return ResponseEntity.ok("User profile updated successfully.");
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }


    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable int userId, @RequestBody ChangePasswordDTO changePasswordDto) {
        userService.changePassword(userId, changePasswordDto);
        return ResponseEntity.ok("Password changed successfully.");
    }
}
