package com.malemate.demo.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MacroStatsDTO {
    private float dailyTarget;
    private float dailyAchieved;
    private float weeklyTarget;
    private float weeklyAchieved;
    private float monthlyTarget;
    private float monthlyAchieved;

    public MacroStatsDTO(float dailyTarget, float dailyAchieved, float weeklyTarget, float weeklyAchieved, float monthlyTarget, float monthlyAchieved)
    {
        this.dailyTarget = dailyTarget;
        this.dailyAchieved = dailyAchieved;
        this.weeklyTarget = weeklyTarget;
        this.weeklyAchieved = weeklyAchieved;
        this.monthlyTarget = monthlyTarget;
        this.monthlyAchieved = monthlyAchieved;
    }
//
//    public float getDailyTarget() { return dailyTarget; }
//    public float getDailyAchieved() { return dailyAchieved; }
//    public float getWeeklyTarget() { return weeklyTarget; }
//    public float getWeeklyAchieved() { return weeklyAchieved; }
//    public float getMonthlyTarget() { return monthlyTarget; }
//    public float getMonthlyAchieved() { return monthlyAchieved; }
}
