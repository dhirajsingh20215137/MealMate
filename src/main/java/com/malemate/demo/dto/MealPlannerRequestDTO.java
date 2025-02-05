package com.malemate.demo.dto;

import lombok.Data;

@Data
public class MealPlannerRequestDTO {
    private int foodId;
    private String mealType;
    private float quantityValue;
}