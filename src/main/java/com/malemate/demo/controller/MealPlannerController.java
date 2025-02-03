package com.malemate.demo.controller;

import com.malemate.demo.dto.MealPlannerRequestDTO;
import com.malemate.demo.dto.MealPlannerResponseDTO;
import com.malemate.demo.service.MealPlannerService;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/meal-planner")
@Slf4j
public class MealPlannerController {

    private final MealPlannerService mealPlannerService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MealPlannerController(MealPlannerService mealPlannerService, JwtUtil jwtUtil) {
        this.mealPlannerService = mealPlannerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add-food")
    public MealPlannerResponseDTO addFoodToMealPlan(
            @PathVariable int userId,
            @RequestBody MealPlannerRequestDTO mealPlannerRequestDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Adding food to meal plan for user: {}", userId);

        MealPlannerResponseDTO response = mealPlannerService.addFoodToMealPlan(userId, mealPlannerRequestDTO, token);
        log.info("Food added to meal plan for user: {} with food ID: {}", userId, mealPlannerRequestDTO.getFoodId());
        return response;
    }

    @DeleteMapping("/food/{foodId}")
    public String removeFoodFromMealPlan(
            @PathVariable int userId,
            @PathVariable int foodId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Removing food with ID: {} from meal plan for user: {}", foodId, userId);

        String response = mealPlannerService.removeFoodFromMealPlan(userId, foodId, token);
        log.info("Food with ID: {} removed from meal plan for user: {}", foodId, userId);
        return response;
    }

    @GetMapping
    public List<MealPlannerResponseDTO> getUserMealPlan(@PathVariable int userId) {
        log.info("Fetching meal plan for user: {}", userId);

        List<MealPlannerResponseDTO> mealPlan = mealPlannerService.getUserMealPlan(userId);
        log.info("Fetched meal plan for user: {} with {} entries", userId, mealPlan.size());
        return mealPlan;
    }
}
