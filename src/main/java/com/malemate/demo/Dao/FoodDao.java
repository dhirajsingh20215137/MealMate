package com.malemate.demo.Dao;

import com.malemate.demo.entity.Food;

import java.util.List;
import java.util.Optional;

public interface FoodDao {

    Optional<Food> getFoodById(int id);


    Food saveFood(Food food);


    void deleteFood(int id);


    List<Food> getAllFoodItems();


    List<Food> getFoodItemsByUserId(int userId);


    List<Food> getFoodItemsByType(String foodType);
}
