package com.malemate.demo.Dao;

import com.malemate.demo.entity.MealPlanner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class MealPlannerImplementation implements MealPlannerDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(MealPlannerImplementation.class);

    @Override
    public void save(MealPlanner mealPlanner) {
        logger.info("Saving meal planner for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        if (mealPlanner.getMealPlannerId() == 0) {
            entityManager.persist(mealPlanner);  // Insert new record
            logger.debug("New meal planner saved for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        } else {
            entityManager.merge(mealPlanner);  // Update existing record
            logger.debug("Meal planner updated for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        }
    }

    @Override
    public void deleteByUserIdAndFoodId(int userId, int foodId) {
        logger.info("Soft deleting meal planner for userId: {}, foodId: {}", userId, foodId);

        Optional<MealPlanner> mealPlannerOpt = findByUserIdAndFoodId(userId, foodId);
        if (mealPlannerOpt.isPresent()) {
            MealPlanner mealPlanner = mealPlannerOpt.get();
            mealPlanner.setDeleted(true);
            entityManager.merge(mealPlanner);  // Soft delete by setting deleted = true
            logger.debug("Soft deleted meal planner for userId: {}, foodId: {}", userId, foodId);
        } else {
            logger.warn("No meal planner entry found for userId: {}, foodId: {}", userId, foodId);
        }
    }

    @Override
    public List<MealPlanner> findByUserId(int userId) {
        logger.info("Finding active meal planners for userId: {}", userId);
        List<MealPlanner> mealPlanners = entityManager.createQuery(
                        "SELECT m FROM MealPlanner m WHERE m.user.userId = :userId AND m.deleted = false", MealPlanner.class)
                .setParameter("userId", userId)
                .getResultList();
        logger.debug("Found {} meal planners for userId: {}", mealPlanners.size(), userId);
        return mealPlanners;
    }

    public Optional<MealPlanner> findByUserIdAndFoodId(int userId, int foodId) {
        logger.info("Finding meal planner for userId: {}, foodId: {}", userId, foodId);
        try {
            MealPlanner mealPlanner = entityManager.createQuery(
                            "SELECT m FROM MealPlanner m WHERE m.user.userId = :userId AND m.food.foodId = :foodId AND m.deleted = false",
                            MealPlanner.class)
                    .setParameter("userId", userId)
                    .setParameter("foodId", foodId)
                    .getSingleResult();
            logger.debug("Meal planner found for userId: {}, foodId: {}", userId, foodId);
            return Optional.of(mealPlanner);
        } catch (Exception e) {
            logger.warn("No active meal planner found for userId: {}, foodId: {}", userId, foodId);
            return Optional.empty();
        }
    }
}
