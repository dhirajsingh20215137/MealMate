package com.malemate.demo.service.impl;

import com.malemate.demo.dto.ChangePasswordDTO;
import com.malemate.demo.dto.UserProfileDTO;
import com.malemate.demo.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserServiceInterface {


    UserProfileDTO getUserProfile(int userId);

    void updateUserProfile(int userId, UserProfileDTO userProfileDTO);

    User uploadProfileImage(MultipartFile file, int userId) throws IOException;

    void deleteUser(int userId);

    void changePassword(int userId, ChangePasswordDTO changePasswordDto);
}
