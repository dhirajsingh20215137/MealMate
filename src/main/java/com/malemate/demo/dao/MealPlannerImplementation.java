package com.malemate.demo.dao;

import com.malemate.demo.entity.MealPlanner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
            entityManager.persist(mealPlanner);
            logger.debug("New meal planner saved for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        } else {
            entityManager.merge(mealPlanner);
            logger.debug("Meal planner updated for userId: {}, foodId: {}", mealPlanner.getUser().getUserId(), mealPlanner.getFood().getFoodId());
        }
    }

    @Override
    public void deleteByUserIdAndmealPlannerId(int userId, int mealPlannerId) {
        logger.info("Soft deleting meal planner for userId: {}, mealPlannerId: {}", userId, mealPlannerId);
        Optional<MealPlanner> mealPlannerOpt = findByUserIdAndmealPlannerId(userId, mealPlannerId);
        if (mealPlannerOpt.isPresent()) {
            MealPlanner mealPlanner = mealPlannerOpt.get();
            mealPlanner.setDeleted(true);
            entityManager.merge(mealPlanner);
            logger.info("Soft deleted meal planner for userId: {}, mealPlannerId: {}", userId, mealPlannerId);
        } else {
            logger.warn("No meal planner entry found for userId: {}, mealPlannerId: {}", userId, mealPlannerId);
        }
    }

    @Override
    public List<MealPlanner> findByUserId(int userId) {
        logger.info("Finding daily meal planners for userId: {}", userId);
        LocalDate today = LocalDate.now();
        List<MealPlanner> mealPlanners = entityManager.createQuery(
                        "SELECT m FROM MealPlanner m WHERE m.user.userId = :userId " +
                                "AND m.deleted = false AND DATE(m.createdAt) = :today", MealPlanner.class)
                .setParameter("userId", userId)
                .setParameter("today", today)
                .getResultList();
        logger.debug("Found {} meal planners for userId: {} on {}", mealPlanners.size(), userId, today);
        return mealPlanners;
    }

    public Optional<MealPlanner> findByUserIdAndmealPlannerId(int userId, int mealPlannerId) {
        logger.info("Finding meal planner for userId: {}, foodId: {}", userId, mealPlannerId);
        try {
            MealPlanner mealPlanner = entityManager.createQuery(
                            "SELECT m FROM MealPlanner m WHERE m.user.userId = :userId AND m.mealPlannerId = :mealPlannerId AND m.deleted = false",
                            MealPlanner.class)
                    .setParameter("userId", userId)
                    .setParameter("mealPlannerId", mealPlannerId)
                    .getSingleResult();

            logger.info("Meal planner found for userId: {}, mealPlannerId: {}", userId, mealPlannerId);
            return Optional.of(mealPlanner);
        } catch (NoResultException e) {
            logger.warn("No active meal planner found for userId: {}, mealPlannerId: {}", userId, mealPlannerId);
            return Optional.empty();
        }
    }
}
