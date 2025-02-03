package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.MealPlannerDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.MealPlannerRequestDTO;
import com.malemate.demo.dto.MealPlannerResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.MealPlanner;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MealPlannerService {

    private final MealPlannerDao mealPlannerDao;
    private final FoodDao foodDao;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public MealPlannerService(MealPlannerDao mealPlannerDao, FoodDao foodDao, UserDao userDao, JwtUtil jwtUtil) {
        this.mealPlannerDao = mealPlannerDao;
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public MealPlannerResponseDTO addFoodToMealPlan(int userId, MealPlannerRequestDTO requestDTO, String token) {
        log.info("Adding food to meal plan for userId: {}, foodId: {}", userId, requestDTO.getFoodId());

        validateMealPlannerRequest(requestDTO, token);

        User user = getAuthenticatedUser(userId, token);
        Food food = foodDao.findById(requestDTO.getFoodId())
                .orElseThrow(() -> {
                    log.error("Food not found for ID: {}", requestDTO.getFoodId());
                    return new IllegalArgumentException("Food not found");
                });

        if (food.getFoodType() == Food.FoodType.CUSTOM_FOOD) {
            if (food.getUser() == null || food.getUser().getUserId() != userId) {
                log.warn("User {} tried adding another user's custom food", userId);
                throw new SecurityException("You cannot add another user's custom food");
            }
        }

        MealPlanner mealPlanner = new MealPlanner();
        mealPlanner.setUser(user);
        mealPlanner.setFood(food);
        mealPlanner.setMealType(MealPlanner.MealType.valueOf(requestDTO.getMealType().toUpperCase()));
        mealPlanner.setQuantityValue(requestDTO.getQuantityValue());

        mealPlannerDao.save(mealPlanner);

        log.info("Food successfully added to meal plan for userId: {}", userId);
        return mapToMealPlannerResponseDTO(mealPlanner);
    }

    public String removeFoodFromMealPlan(int userId, int foodId, String token) {
        log.info("Removing food from meal plan for userId: {}, foodId: {}", userId, foodId);

        User user = getAuthenticatedUser(userId, token);

        mealPlannerDao.deleteByUserIdAndFoodId(user.getUserId(), foodId);
        log.info("Food removed from meal plan for userId: {}", userId);

        return "Food removed from meal plan";
    }

    public List<MealPlannerResponseDTO> getUserMealPlan(int userId) {
        log.info("Fetching meal plan for userId: {}", userId);

        List<MealPlanner> mealPlans = mealPlannerDao.findByUserId(userId);
        return mealPlans.stream()
                .map(this::mapToMealPlannerResponseDTO)
                .collect(Collectors.toList());
    }

    private MealPlannerResponseDTO mapToMealPlannerResponseDTO(MealPlanner mealPlanner) {
        return new MealPlannerResponseDTO(
                mealPlanner.getMealPlannerId(),
                mealPlanner.getUser().getUserId(),
                mealPlanner.getFood().getFoodId(),
                mealPlanner.getFood().getFoodName(),
                mealPlanner.getMealType().name(),
                mealPlanner.getQuantityValue()
        );
    }

    private User getAuthenticatedUser(int userId, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new IllegalArgumentException("User not found");
                });

        if (user.getUserId() != userId) {
            log.warn("Unauthorized access attempt by userId: {}", user.getUserId());
            throw new SecurityException("Unauthorized action");
        }

        return user;
    }

    private void validateMealPlannerRequest(MealPlannerRequestDTO requestDTO, String token) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new IllegalArgumentException("Authentication token is required");
        }
        if (requestDTO == null) {
            log.error("MealPlannerRequestDTO is null");
            throw new IllegalArgumentException("Meal plan request cannot be null");
        }
        if (requestDTO.getFoodId() <= 0) {
            log.error("Invalid foodId: {}", requestDTO.getFoodId());
            throw new IllegalArgumentException("Invalid food ID");
        }
        if (StringUtils.isBlank(requestDTO.getMealType())) {
            log.error("Meal type is missing");
            throw new IllegalArgumentException("Meal type is required");
        }
        if (requestDTO.getQuantityValue() <= 0) {
            log.error("Invalid quantity value: {}", requestDTO.getQuantityValue());
            throw new IllegalArgumentException("Quantity value must be greater than zero");
        }
    }
}
