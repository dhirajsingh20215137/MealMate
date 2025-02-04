package com.malemate.demo.Dao;

import com.malemate.demo.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@Log4j2
public class FoodDaoImplementation implements FoodDao {

   // private static final Logger log = LoggerFactory.getLogger(FoodDaoImplementation.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Food> findById(int id) {
        log.info("Finding food item by id: {}", id);
        Food food = entityManager.find(Food.class, id);
        if (food != null) {
            log.info("Food item found: {}", food.getFoodName());
        } else {
            log.warn("Food item with id: {} not found", id);
        }
        return Optional.ofNullable(food);
    }

    @Override
    public Food save(Food food) {
        if (food.getFoodId() == 0) {
            log.info("Persisting new food item: {}", food.getFoodName());
            entityManager.persist(food);
        } else {
            log.info("Merging existing food item: {}", food.getFoodName());
            food = entityManager.merge(food);
        }
        log.info("Food item saved: {}", food.getFoodName());
        return food;
    }

    @Override
    public void delete(Food food) {
        log.info("Deleting food item: {}", food.getFoodName());
        if (entityManager.contains(food)) {
            entityManager.remove(food);
            log.info("Food item deleted: {}", food.getFoodName());
        } else {
            entityManager.remove(entityManager.merge(food));
            log.info("Food item deleted after merge: {}", food.getFoodName());
        }
    }

    @Override
    public List<Food> getAllFoodItems() {
        log.info("Fetching all food items from the database");
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f", Food.class).getResultList();
        log.info("Fetched {} food items", foodItems.size());
        return foodItems;
    }

    @Override
    public List<Food> getFoodItemsByUserId(int userId) {
        log.info("Fetching food items for user with id: {}", userId);
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f WHERE f.user.id = :userId", Food.class)
                .setParameter("userId", userId)
                .getResultList();
        log.info("Fetched {} food items for user id: {}", foodItems.size(), userId);
        return foodItems;
    }

    @Override
    public List<Food> getFoodItemsByType(Food.FoodType foodType) {
        log.info("Fetching food items of type: {}", foodType);
        List<Food> foodItems = entityManager.createQuery("SELECT f FROM Food f WHERE f.foodType = :foodType", Food.class)
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
