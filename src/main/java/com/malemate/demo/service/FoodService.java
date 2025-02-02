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
        User user = getAuthenticatedUser(token);

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can add food items");
        }

        Food food = mapToFoodEntity(foodDTO, user);
        foodDao.save(food);
        return mapToFoodResponseDTO(food);
    }

    public FoodResponseDTO updateFood(int foodId, FoodDTO foodDTO, String token) {
        User user = getAuthenticatedUser(token);

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can update food items");
        }

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        updateFoodEntity(food, foodDTO);
        foodDao.save(food);
        return mapToFoodResponseDTO(food);
    }

    public void deleteFood(int foodId, String token) {
        User user = getAuthenticatedUser(token);

        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            throw new RuntimeException("Only admins can delete food items");
        }

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        foodDao.delete(food);
    }

    public List<FoodResponseDTO> getAllFoodItems() {
        List<Food> foods = foodDao.getAllFoodItems();
        return foods.stream().map(this::mapToFoodResponseDTO).toList();
    }

    private User getAuthenticatedUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Food mapToFoodEntity(FoodDTO foodDTO, User user) {
        Food food = new Food();
        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());
        food.setUser(user);
        return food;
    }

    private void updateFoodEntity(Food food, FoodDTO foodDTO) {
        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());
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
