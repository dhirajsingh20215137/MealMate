package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;

import java.util.List;
import java.util.Optional;

public interface MealPlannerDao {

    void save(MealPlanner mealPlanner);

    // Get a specific meal planner entry by ID
    Optional<MealPlanner> getMealPlannerById(int id);

    // Get all meal planner entries for a specific user
    List<MealPlanner> getAllMealPlansForUser(int userId);

    // Remove a specific food from the user's meal planner
    void removeFoodFromMealPlan(int foodId, int userId);

    // Get all foods from a specific meal type (e.g., breakfast, lunch, dinner, snacks)
    List<MealPlanner> getMealPlanByType(int userId, String mealType);
}
