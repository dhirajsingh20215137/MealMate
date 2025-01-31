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
    private EntityManager entityManager;

    @Override
    public Optional<Food> getFoodById(int id) {
        return Optional.ofNullable(entityManager.find(Food.class, id));
    }

    @Override
    public Food saveFood(Food food) {
        if (food.getFoodId() == 0) {
            entityManager.persist(food);
        } else {
            entityManager.merge(food);
        }
        return food;
    }

    @Override
    public void deleteFood(int id) {
        Food food = getFoodById(id).orElseThrow(() -> new RuntimeException("Food not found"));
        entityManager.remove(food);
    }

    @Override
    public List<Food> getAllFoodItems() {
        return entityManager.createQuery("SELECT f FROM Food f", Food.class).getResultList();
    }

    @Override
    public List<Food> getFoodItemsByUserId(int userId) {
        return entityManager.createQuery("SELECT f FROM Food f WHERE f.user.id = :userId", Food.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Food> getFoodItemsByType(String foodType) {
        return entityManager.createQuery("SELECT f FROM Food f WHERE f.foodType = :foodType", Food.class)
                .setParameter("foodType", foodType)
                .getResultList();
    }

    @Override
    public void updateFood(Food food) {
        if (getFoodById(food.getFoodId()).isPresent()) {
            entityManager.merge(food);
        } else {
            throw new RuntimeException("Food item not found for update");
        }
    }
}
