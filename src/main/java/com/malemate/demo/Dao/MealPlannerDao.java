package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;

import java.util.List;

public interface MealPlannerDao {
    void save(MealPlanner mealPlanner);
    void deleteByUserIdAndFoodId(int userId, int foodId);
    List<MealPlanner> findByUserId(int userId);
}
