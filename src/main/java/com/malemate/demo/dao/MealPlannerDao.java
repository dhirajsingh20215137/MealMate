package com.malemate.demo.dao;

import com.malemate.demo.entity.MealPlanner;

import java.util.List;

public interface MealPlannerDao {
    void save(MealPlanner mealPlanner);
    void deleteByUserIdAndmealPlannerId(int userId, int foodId);
    List<MealPlanner> findByUserId(int userId);
}
