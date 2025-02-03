package com.malemate.demo.Dao;

import com.malemate.demo.entity.Food;
import java.util.List;
import java.util.Optional;

public interface FoodDao {
    Optional<Food> findById(int id);
    Food save(Food food);
    void delete(Food food);
    List<Food> getAllFoodItems();
    List<Food> getFoodItemsByUserId(int userId);
    List<Food> getFoodItemsByType(Food.FoodType foodType);
    Food update(Food food);
}
