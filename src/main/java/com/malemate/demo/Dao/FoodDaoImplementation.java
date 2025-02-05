package com.malemate.demo.Dao;

import com.malemate.demo.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Log4j2
public class FoodDaoImplementation implements FoodDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Food> findById(int id) {
        log.info("Finding food item by id: {}", id);
        Food food = entityManager.find(Food.class, id);

        if (food == null || food.isDeleted()) {
            log.warn("Food item with id: {} not found or is deleted", id);
            return Optional.empty();
        }

        log.info("Food item found: {}", food.getFoodName());
        return Optional.of(food);
    }

    @Override
    public Food save(Food food) {
        if (food.getFoodId() == 0) {
            log.info("Persisting new food item: {}", food.getFoodName());
            entityManager.persist(food);
        } else {
            log.info("Merging existing food item with id: {}", food.getFoodId());
            food = entityManager.merge(food);
        }

        log.info("Food item saved with id: {}", food.getFoodId());
        return food;
    }

    @Override
    public void delete(Food food) {
        log.info("Marking food item as deleted: {}", food.getFoodName());
        Optional<Food> existingFood = findById(food.getFoodId());

        if (existingFood.isPresent()) {
            existingFood.get().setDeleted(true);
            entityManager.merge(existingFood.get());
            log.info("Food item soft deleted: {}", food.getFoodName());
        } else {
            log.warn("Food item with id: {} not found for deletion", food.getFoodId());
        }
    }

    @Override
    public List<Food> getAllFoodItems() {
        log.info("Fetching all active food items");
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f WHERE f.deleted = false", Food.class)
                .getResultList();
        log.info("Fetched {} food items", foodItems.size());
        return foodItems;
    }

    @Override
    public List<Food> getFoodItemsByUserId(int userId) {
        log.info("Fetching food items for user with id: {}", userId);
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f WHERE f.user.id = :userId AND f.deleted = false", Food.class)
                .setParameter("userId", userId)
                .getResultList();
        log.info("Fetched {} food items for user id: {}", foodItems.size(), userId);
        return foodItems;
    }

    @Override
    public List<Food> getFoodItemsByType(Food.FoodType foodType) {
        log.info("Fetching food items of type: {}", foodType);
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f WHERE f.foodType = :foodType AND f.deleted = false", Food.class)
                .setParameter("foodType", foodType)
                .getResultList();
        log.info("Fetched {} food items of type: {}", foodItems.size(), foodType);
        return foodItems;
    }

    @Override
    public Food update(Food food) {
        log.info("Updating food item with id: {}", food.getFoodId());

        if (findById(food.getFoodId()).isPresent()) {
            Food updatedFood = entityManager.merge(food);
            log.info("Food item updated: {}", updatedFood.getFoodName());
            return updatedFood;
        } else {
            log.error("Food item with id: {} not found for update", food.getFoodId());
            throw new RuntimeException("Food item not found for update");
        }
    }
}
