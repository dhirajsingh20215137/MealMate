package com.malemate.demo.controller;

import com.malemate.demo.entity.User;
import com.malemate.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/user/{userId}/photo")
@CrossOrigin(origins = "http://localhost:5173")
public class PhotoController {

    private final UserService userService;

    public PhotoController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Upload Profile Photo
    @PostMapping("/upload-photo")
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
