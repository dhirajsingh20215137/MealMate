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
public class UserFoodService {

    private final FoodDao foodDao;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserFoodService(FoodDao foodDao, UserDao userDao, JwtUtil jwtUtil) {
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public FoodResponseDTO addUserFood(FoodDTO foodDTO, String token, int userId) {
        String username = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("You can only add food items to your own collection");
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

        foodDao.save(food);
        return mapToFoodResponseDTO(food);
    }

    public FoodResponseDTO updateUserFood(int foodId, FoodDTO foodDTO, String token,int userId) {
        String username = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("You can only add food items to your own collection");
        }

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        if (!food.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own food items");
        }

        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());

        foodDao.save(food);
        return mapToFoodResponseDTO(food);
    }

    public void deleteUserFood(int foodId, String token, int userId) {
        String username = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("You can only add food items to your own collection");
        }

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        if (!food.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own food items");
        }

        foodDao.delete(food);
    }

    public List<FoodResponseDTO> getUserFoodItems(String token,int userId) {
        String username = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("You can only add food items to your own collection");
        }

        List<Food> userFoods = foodDao.getFoodItemsByUserId(user.getUserId());
        return userFoods.stream().map(this::mapToFoodResponseDTO).toList();
    }

    private FoodResponseDTO mapToFoodResponseDTO(Food food) {
        return FoodResponseDTO.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .calories(food.getCalories())
                .proteins(food.getProteins())
                .carbs(food.getCarbs())
                .quantityUnit(food.getQuantityUnit().name())
                .foodType(food.getFoodType().name())
                .imageUrl(food.getImageUrl())
                .build();
    }
}
