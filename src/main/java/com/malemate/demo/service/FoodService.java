package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodDao foodDao;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public FoodService(FoodDao foodDao, UserDao userDao, JwtUtil jwtUtil) {
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public FoodResponseDTO addFood(FoodDTO foodDTO, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can add food items");
        }

        Food food = new Food();
        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());
        food.setUser(user);

        foodDao.saveFood(food);
        return mapToFoodResponseDTO(food);
    }

    public FoodResponseDTO updateFood(int foodId, FoodDTO foodDTO, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can update food items");
        }

        Food food = foodDao.getFoodById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());

        foodDao.saveFood(food);
        return mapToFoodResponseDTO(food);
    }

    public void deleteFood(int foodId, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can delete food items");
        }

        foodDao.deleteFood(foodId);
    }

    public List<FoodResponseDTO> getAllFoodItems() {
        List<Food> foods = foodDao.getAllFoodItems();
        return foods.stream().map(this::mapToFoodResponseDTO).toList();
    }

    private FoodResponseDTO mapToFoodResponseDTO(Food food) {
        FoodResponseDTO dto = new FoodResponseDTO();
        dto.setFoodId(food.getFoodId());
        dto.setFoodName(food.getFoodName());
        dto.setCalories(food.getCalories());
        dto.setProteins(food.getProteins());
        dto.setCarbs(food.getCarbs());
        dto.setQuantityUnit(food.getQuantityUnit().name());
        dto.setFoodType(food.getFoodType().name());
        dto.setImageUrl(food.getImageUrl());
        return dto;
    }
}
