package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.User;
import com.malemate.demo.exceptions.*;
import com.malemate.demo.exceptions.BadRequestException;
import com.malemate.demo.exceptions.ResourceNotFoundException;
import com.malemate.demo.exceptions.UnauthorizedException;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
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
        log.info("Adding food to user collection for userId: {}", userId);

        validateUserFoodRequest(foodDTO, token, userId);

        User user = getAuthenticatedUser(userId, token);

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
        log.info("Food added successfully to userId: {}", userId);

        return mapToFoodResponseDTO(food);
    }

    public FoodResponseDTO updateUserFood(int foodId, FoodDTO foodDTO, String token, int userId) {
        log.info("Updating food for userId: {}, foodId: {}", userId, foodId);

        validateUserFoodRequest(foodDTO, token, userId);

        User user = getAuthenticatedUser(userId, token);

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> {
                    log.error("Food item not found for foodId: {}", foodId);
                    return new ResourceNotFoundException("Food item not found");
                });

        if (!food.getUser().equals(user)) {
            log.warn("Unauthorized food update attempt for userId: {}", userId);
            throw new UnauthorizedException("You can only update your own food items");
        }

        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setImageUrl(foodDTO.getImageUrl());

        foodDao.save(food);
        log.info("Food updated successfully for userId: {}", userId);

        return mapToFoodResponseDTO(food);
    }

    public void deleteUserFood(int foodId, String token, int userId) {
        log.info("Deleting food for userId: {}, foodId: {}", userId, foodId);

        User user = getAuthenticatedUser(userId, token);

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> {
                    log.error("Food item not found for foodId: {}", foodId);
                    return new ResourceNotFoundException("Food item not found");
                });

        if (!food.getUser().equals(user)) {
            log.warn("Unauthorized food delete attempt for userId: {}", userId);
            throw new UnauthorizedException("You can only delete your own food items");
        }


        food.setDeleted(true);
        foodDao.save(food);
        log.info("Food deleted successfully for userId: {}", userId);
    }

    public List<FoodResponseDTO> getUserFoodItems(String token, int userId) {
        log.info("Fetching food items for userId: {}", userId);


        User user = getAuthenticatedUser(userId, token);

        List<Food> allFoods = Stream.concat(
                        foodDao.getFoodItemsByType(Food.FoodType.UNIVERSAL_FOOD).stream(),
                        foodDao.getFoodItemsByUserId(user.getUserId()).stream()
                )
//                .filter(food -> !food.isDeleted())
                .toList();

        return allFoods.stream().map(this::mapToFoodResponseDTO).toList();
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

    private User getAuthenticatedUser(int userId, String token) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new SecurityException("Authentication token is required");
        }

        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        if (user.getUserId() != userId) {
            log.warn("Unauthorized access attempt by userId: {}", userId);
            throw new UnauthorizedException("Unauthorized action");
        }

        return user;
    }

    private void validateUserFoodRequest(FoodDTO foodDTO, String token, int userId) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new BadRequestException("Authentication token is required");
        }
        if (foodDTO == null) {
            log.error("FoodDTO is null");
            throw new BadRequestException("Food details cannot be null");
        }
        if (StringUtils.isBlank(foodDTO.getFoodName())) {
            log.error("Food name is missing");
            throw new BadRequestException("Food name is required");
        }
        if (foodDTO.getCalories() <= 0) {
            log.error("Invalid calories value: {}", foodDTO.getCalories());
            throw new BadRequestException("Calories must be greater than zero");
        }
        if (foodDTO.getProteins() <= 0) {
            log.error("Invalid proteins value: {}", foodDTO.getProteins());
            throw new BadRequestException("Proteins must be greater than zero");
        }
        if (foodDTO.getCarbs() <= 0) {
            log.error("Invalid carbs value: {}", foodDTO.getCarbs());
            throw new BadRequestException("Carbs must be greater than zero");
        }
        if (StringUtils.isBlank(foodDTO.getFoodType())) {
            log.error("Food type is missing");
            throw new BadRequestException("Food type is required");
        }
        if (StringUtils.isBlank(foodDTO.getQuantityUnit())) {
            log.error("Quantity unit is missing");
            throw new BadRequestException("Quantity unit is required");
        }
    }
}
