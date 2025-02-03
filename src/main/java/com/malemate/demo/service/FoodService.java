package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

@Log
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
        log.info("Add food request received for: " + foodDTO.getFoodName());

        validateFoodDTO(foodDTO);
        User user = getAuthenticatedUser(token);
        validateAdminUser(user);

        Food food = mapToFoodEntity(foodDTO, user);
        foodDao.save(food);

        log.info("Food added successfully: " + food.getFoodName());
        return mapToFoodResponseDTO(food);
    }

    public FoodResponseDTO updateFood(int foodId, FoodDTO foodDTO, String token) {
        log.info("Update food request received for ID: " + foodId);

        validateFoodDTO(foodDTO);
        User user = getAuthenticatedUser(token);
        validateAdminUser(user);

        Food food = validateFoodExists(foodId);
        updateFoodEntity(food, foodDTO);
        foodDao.save(food);

        log.info("Food updated successfully: " + food.getFoodName());
        return mapToFoodResponseDTO(food);
    }

    public void deleteFood(int foodId, String token) {
        log.info("Delete food request received for ID: " + foodId);

        User user = getAuthenticatedUser(token);
        validateAdminUser(user);

        Food food = validateFoodExists(foodId);
        foodDao.delete(food);

        log.info("Food deleted successfully: " + food.getFoodName());
    }

    public List<FoodResponseDTO> getAllFoodItems() {
        log.info("Fetching all food items");

        List<Food> foods = foodDao.getAllFoodItems();
        return foods.stream().map(this::mapToFoodResponseDTO).toList();
    }

    private User getAuthenticatedUser(String token) {
        log.info("Extracting user from token");
        String email = jwtUtil.extractEmail(token);

        return userDao.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.severe("User not found for email: " + email);
                    return new RuntimeException("User not found");
                });
    }

    private void validateAdminUser(User user) {
        if (!user.getUserType().equals(User.UserType.ADMIN)) {
            log.warning("Unauthorized access attempt by user: " + user.getEmail());
            throw new RuntimeException("Only admins can perform this action");
        }
    }

    private Food validateFoodExists(int foodId) {
        return foodDao.findById(foodId)
                .orElseThrow(() -> {
                    log.severe("Food not found with ID: " + foodId);
                    return new RuntimeException("Food item not found");
                });
    }

    private void validateFoodDTO(FoodDTO foodDTO) {
        if (StringUtils.isBlank(foodDTO.getFoodName())) {
            throw new IllegalArgumentException("Food name is required");
        }
        if (Objects.isNull(foodDTO.getCalories()) || foodDTO.getCalories() <= 0) {
            throw new IllegalArgumentException("Calories must be greater than 0");
        }
        if (Objects.isNull(foodDTO.getProteins()) || foodDTO.getProteins() < 0) {
            throw new IllegalArgumentException("Proteins must be 0 or greater");
        }
        if (Objects.isNull(foodDTO.getCarbs()) || foodDTO.getCarbs() < 0) {
            throw new IllegalArgumentException("Carbs must be 0 or greater");
        }
        if (StringUtils.isBlank(foodDTO.getFoodType())) {
            throw new IllegalArgumentException("Food type is required");
        }
        if (StringUtils.isBlank(foodDTO.getQuantityUnit())) {
            throw new IllegalArgumentException("Quantity unit is required");
        }
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
