package com.malemate.demo.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Repository
public class MacroDaoImplementation implements MacroDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public float getUserMacroTarget(int userId, String macroType) {
        String field = getMacroTargetField(macroType);
        String jpql = "SELECT u." + field + " FROM User u WHERE u.userId = :userId";
        TypedQuery<Float> query = entityManager.createQuery(jpql, Float.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    @Override
    public float getDailyAchievedMacro(int userId, String macroType, LocalDate date) {
        return getAchievedMacro(userId, macroType, date, date);
    }

    @Override
    public float getWeeklyAchievedMacro(int userId, String macroType, LocalDate date) {
        LocalDate startOfWeek = date.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return getAchievedMacro(userId, macroType, startOfWeek, endOfWeek);
    }

    @Override
    public float getMonthlyAchievedMacro(int userId, String macroType, LocalDate date) {
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return getAchievedMacro(userId, macroType, startOfMonth, endOfMonth);
    }

    private float getAchievedMacro(int userId, String macroType, LocalDate startDate, LocalDate endDate) {
        String field = getMacroField(macroType);
        String jpql = "SELECT SUM(m.food." + field + " * m.quantityValue) FROM MealPlanner m " +
                "WHERE m.user.userId = :userId AND m.createdAt BETWEEN :startDate AND :endDate";

        TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate.atStartOfDay());
        query.setParameter("endDate", endDate.atTime(23, 59, 59));

        Double result = query.getSingleResult();
        return (result != null) ? result.floatValue() : 0f;
    }


    private String getMacroTargetField(String macroType) {
        switch (macroType) {
            case "carbs": return "targetedCarbs";
            case "proteins": return "targetedProtein";
            case "calories": return "targetedCalories";
            default: throw new IllegalArgumentException("Invalid macro type");
        }
    }

    private String getMacroField(String macroType) {
        switch (macroType) {
            case "carbs": return "carbs";
            case "proteins": return "proteins";
            case "calories": return "calories";
            default: throw new IllegalArgumentException("Invalid macro type");
        }
    }
}
