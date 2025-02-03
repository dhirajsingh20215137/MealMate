package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class MealPlannerImplementation implements MealPlannerDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(MealPlannerImplementation.class);

    @Override
    public void save(MealPlanner mealPlanner) {
        logger.info("Saving meal planner for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        entityManager.persist(mealPlanner);
        logger.debug("Meal planner saved for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
    }

    @Override
    public void deleteByUserIdAndFoodId(int userId, int foodId) {
        logger.info("Deleting meal planner for userId: {}, foodId: {}", userId, foodId);
        int rowsDeleted = entityManager.createQuery("DELETE FROM MealPlanner m WHERE m.user.userId = :userId AND m.food.foodId = :foodId")
                .setParameter("userId", userId)
                .setParameter("foodId", foodId)
                .executeUpdate();
        logger.debug("Deleted {} rows for userId: {} and foodId: {}", rowsDeleted, userId, foodId);
    }

    @Override
    public List<MealPlanner> findByUserId(int userId) {
        logger.info("Finding meal planners for userId: {}", userId);
        List<MealPlanner> mealPlanners = entityManager.createQuery("SELECT m FROM MealPlanner m WHERE m.user.userId = :userId", MealPlanner.class)
                .setParameter("userId", userId)
                .getResultList();
        logger.debug("Found {} meal planners for userId: {}", mealPlanners.size(), userId);
        return mealPlanners;
    }
}
