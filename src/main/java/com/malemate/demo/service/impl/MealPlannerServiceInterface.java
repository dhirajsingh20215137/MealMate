package com.malemate.demo.service.impl;

import com.malemate.demo.dto.MealPlannerRequestDTO;
import com.malemate.demo.dto.MealPlannerResponseDTO;

import java.util.List;

public interface MealPlannerServiceInterface {
    MealPlannerResponseDTO addFoodToMealPlan(int userId, MealPlannerRequestDTO requestDTO, String token);
    String removeFoodFromMealPlan(int userId, int mealPlannerId, String token);
    List<MealPlannerResponseDTO> getUserMealPlan(int userId);
}
