package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class MealPlannerImplementation implements MealPlannerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(MealPlanner mealPlanner) {
        entityManager.persist(mealPlanner);
    }

    @Override
    public void deleteByUserIdAndFoodId(int userId, int foodId) {
        entityManager.createQuery("DELETE FROM MealPlanner m WHERE m.user.userId = :userId AND m.food.foodId = :foodId")
                .setParameter("userId", userId)
                .setParameter("foodId", foodId)
                .executeUpdate();
    }

    @Override
    public List<MealPlanner> findByUserId(int userId) {
        return entityManager.createQuery("SELECT m FROM MealPlanner m WHERE m.user.userId = :userId", MealPlanner.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
