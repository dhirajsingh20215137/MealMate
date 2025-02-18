package com.malemate.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDTO {
    private String foodName;
    private float fats;
    private float proteins;
    private float carbs;
    private String quantityUnit;
    private String foodType;
   private String imageUrl;


}
