package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public class MealPlannerImplementation implements MealPlannerDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(MealPlanner mealPlanner) {
        if (mealPlanner.getMealPlannerId() == 0) {
            entityManager.persist(mealPlanner); // If it's a new entry, persist it.
        } else {
            entityManager.merge(mealPlanner); // If it's an existing entry, update it.
        }
    }

    @Override
    public Optional<MealPlanner> getMealPlannerById(int id) {
        MealPlanner mealPlanner = entityManager.find(MealPlanner.class, id);
        return Optional.ofNullable(mealPlanner); // Return the mealPlanner if found, otherwise an empty Optional.
    }

    @Override
    public List<MealPlanner> getAllMealPlansForUser(int userId) {
        // Query to get all meal plans for the specific user
        return entityManager.createQuery("SELECT mp FROM MealPlanner mp WHERE mp.user.userId = :userId", MealPlanner.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Transactional
    public void removeFoodFromMealPlan(int foodId, int userId) {
        // Query to delete a food item from the meal plan
        MealPlanner mealPlanner = entityManager.createQuery("SELECT mp FROM MealPlanner mp WHERE mp.food.foodId = :foodId AND mp.user.userId = :userId", MealPlanner.class)
                .setParameter("foodId", foodId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (mealPlanner != null) {
            entityManager.remove(mealPlanner); // Remove the meal planner entry with the given foodId and userId.
        }
    }

    @Override
    public List<MealPlanner> getMealPlanByType(int userId, String mealType) {
        // Query to get meal plans of a specific type (e.g., breakfast, lunch, etc.)
        return entityManager.createQuery("SELECT mp FROM MealPlanner mp WHERE mp.user.userId = :userId AND mp.mealType = :mealType", MealPlanner.class)
                .setParameter("userId", userId)
                .setParameter("mealType", mealType)
                .getResultList();
    }

}
