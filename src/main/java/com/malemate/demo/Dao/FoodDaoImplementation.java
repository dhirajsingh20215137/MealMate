package com.malemate.demo.Dao;

import com.malemate.demo.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class FoodDaoImplementation implements FoodDao {


    @PersistenceContext
    private EntityManager entityManager; // Injecting EntityManager

    @Override
    public Optional<Food> getFoodById(int id) {
        Food food = entityManager.find(Food.class, id);
        return Optional.ofNullable(food); // Return food if found, else return Optional.empty()
    }



    @Override
    public Food saveFood(Food food) {
        if (food.getFoodId() == 0) {
            // If the foodId is 0 (not set), it's a new food, so persist it
            entityManager.persist(food);
        } else {
            // If the foodId is set, merge the food (update existing food item)
            entityManager.merge(food);
        }
        return food; // Return the saved or updated food
    }

    @Override
    public void deleteFood(int id) {
        Food food = getFoodById(id).orElseThrow(() -> new RuntimeException("Food not found"));
        entityManager.remove(food); // Remove food item from the database
    }

    @Override
    public List<Food> getAllFoodItems() {
        // Custom query to get all food items (both universal and custom)
        return entityManager.createQuery("SELECT f FROM Food f", Food.class).getResultList();
    }

    @Override
    public List<Food> getFoodItemsByUserId(int userId) {
        // Custom query to get food items by userId (custom foods for a user)
        return entityManager.createQuery("SELECT f FROM Food f WHERE f.user.id = :userId", Food.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Food> getFoodItemsByType(String foodType) {
        // Custom query to get food items by food type (universal or custom)
        return entityManager.createQuery("SELECT f FROM Food f WHERE f.foodType = :foodType", Food.class)
                .setParameter("foodType", foodType)
                .getResultList();
    }






}
