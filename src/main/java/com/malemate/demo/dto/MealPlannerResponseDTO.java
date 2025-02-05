package com.malemate.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class MealPlannerResponseDTO {
    private int mealPlannerId;
    private int userId;
    private int foodId;
    private String foodName;
    private String mealType;
    private float quantityValue;

    public MealPlannerResponseDTO(int mealPlannerId, int userId, int foodId, String foodName, String mealType, float quantityValue) {
        this.mealPlannerId = mealPlannerId;
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.mealType = mealType;
        this.quantityValue = quantityValue;
    }
}