package com.malemate.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Repository
public class MacroDaoImplementation implements MacroDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(MacroDaoImplementation.class);

    @Override
    public float getUserMacroTarget(int userId, String macroType) {
        logger.info("Getting macro target for userId: {}, macroType: {}", userId, macroType);
        String field = getMacroTargetField(macroType);
        String jpql = "SELECT u." + field + " FROM User u WHERE u.userId = :userId AND u.deleted = false";
        TypedQuery<Float> query = entityManager.createQuery(jpql, Float.class);
        query.setParameter("userId", userId);
        Float result = query.getSingleResult();
        logger.debug("Macro target for userId: {} and macroType: {} is: {}", userId, macroType, result);
        return result != null ? result : 0f;
    }

    @Override
    public float getDailyAchievedMacro(int userId, String macroType, LocalDate date) {
        logger.info("Getting daily achieved macro for userId: {}, macroType: {}, date: {}", userId, macroType, date);
        return getAchievedMacro(userId, macroType, date, date);
    }

    @Override
    public float getWeeklyAchievedMacro(int userId, String macroType, LocalDate date) {
        logger.info("Getting weekly achieved macro for userId: {}, macroType: {}, week starting: {}", userId, macroType, date);
        LocalDate startOfWeek = date.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return getAchievedMacro(userId, macroType, startOfWeek, endOfWeek);
    }

    @Override
    public float getMonthlyAchievedMacro(int userId, String macroType, LocalDate date) {
        logger.info("Getting monthly achieved macro for userId: {}, macroType: {}, month: {}", userId, macroType, date.getMonth());
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return getAchievedMacro(userId, macroType, startOfMonth, endOfMonth);
    }

    private float getAchievedMacro(int userId, String macroType, LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating achieved macro for userId: {}, macroType: {}, startDate: {}, endDate: {}", userId, macroType, startDate, endDate);
        String field = getMacroField(macroType);
        String jpql = "SELECT SUM(m.food." + field + " * m.quantityValue) FROM MealPlanner m " +
                "WHERE m.user.userId = :userId " +
                "AND m.deleted = false " +  // Ensure soft delete is respected
                "AND m.food.deleted = false " + // Ignore deleted foods
                "AND m.createdAt BETWEEN :startDate AND :endDate";
        TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate.atStartOfDay());
        query.setParameter("endDate", endDate.atTime(23, 59, 59));
        Double result = query.getSingleResult();
        float achievedMacro = (result != null) ? result.floatValue() : 0f;

        logger.debug("Achieved macro for userId: {}, macroType: {}, startDate: {}, endDate: {} is: {}",
                userId, macroType, startDate, endDate, achievedMacro);

        return achievedMacro;
    }

    private String getMacroTargetField(String macroType) {
        switch (macroType) {
            case "carbs": return "targetedCarbs";
            case "proteins": return "targetedProtein";
            case "calories": return "targetedCalories";
            default: throw new IllegalArgumentException("Invalid macro type: " + macroType);
        }
    }

    private String getMacroField(String macroType) {
        switch (macroType) {
            case "carbs": return "carbs";
            case "proteins": return "proteins";
            case "calories": return "calories";
            default: throw new IllegalArgumentException("Invalid macro type: " + macroType);
        }
    }
}
