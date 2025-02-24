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
    private String mealType;  // Changed to String
    private float quantityValue;

    // Food details
    private float fats;
    private float proteins;
    private float carbs;
    private String quantityUnit; // Changed to String
    private String foodType; // Changed to String
    private String imageUrl;

    public MealPlannerResponseDTO(int mealPlannerId, int userId, int foodId, String foodName,
                                  String mealType, float quantityValue, float fats, float proteins,
                                  float carbs, String quantityUnit, String foodType, String imageUrl) {
        this.mealPlannerId = mealPlannerId;
        this.userId = userId;
        this.foodId = foodId;
        this.foodName = foodName;
        this.mealType = mealType;
        this.quantityValue = quantityValue;
        this.fats = fats;
        this.proteins = proteins;
        this.carbs = carbs;
        this.quantityUnit = quantityUnit;
        this.foodType = foodType;
        this.imageUrl = imageUrl;
    }
}
