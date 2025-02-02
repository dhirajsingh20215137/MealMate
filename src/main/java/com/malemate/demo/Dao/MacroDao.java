package com.malemate.demo.Dao;

import java.time.LocalDate;

public interface MacroDao {
    float getUserMacroTarget(int userId, String macroType);
    float getDailyAchievedMacro(int userId, String macroType, LocalDate date);
    float getWeeklyAchievedMacro(int userId, String macroType, LocalDate date);
    float getMonthlyAchievedMacro(int userId, String macroType, LocalDate date);
}
