package com.malemate.demo.service.impl;

import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserFoodServiceInterface {
    FoodResponseDTO addUserFood(FoodDTO foodDTO, String token, int userId, MultipartFile file);

    Food uploadFoodImage(MultipartFile file, int userId) throws IOException;

    FoodResponseDTO updateUserFood(int foodId, FoodDTO foodDTO, String token, int userId);

    void deleteUserFood(int foodId, String token, int userId);

    List<FoodResponseDTO> getUserFoodItems(String token, int userId);
}
