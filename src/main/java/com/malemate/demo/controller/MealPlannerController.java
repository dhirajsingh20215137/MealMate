package com.malemate.demo.controller;

import com.malemate.demo.dto.MealPlannerRequestDTO;
import com.malemate.demo.dto.MealPlannerResponseDTO;
import com.malemate.demo.service.MealPlannerService;
import com.malemate.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/meal-planner")
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
        return mealPlannerService.addFoodToMealPlan(userId, mealPlannerRequestDTO, token);
    }

    @DeleteMapping("/food/{foodId}")
    public String removeFoodFromMealPlan(
            @PathVariable int userId,
            @PathVariable int foodId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return mealPlannerService.removeFoodFromMealPlan(userId, foodId, token);
    }

    // Get meal plan for a user
    @GetMapping
    public List<MealPlannerResponseDTO> getUserMealPlan(@PathVariable int userId){
      //  String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return mealPlannerService.getUserMealPlan(userId);
    }
}
