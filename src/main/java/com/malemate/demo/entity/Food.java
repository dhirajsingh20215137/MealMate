package com.malemate.demo.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "food")
public class Food extends base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private int foodId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "calories", nullable = false)
    private float calories;

    @Column(name = "proteins", nullable = false)
    private float proteins;

    @Column(name = "carbs", nullable = false)
    private float carbs;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_unit", nullable = false)
    private QuantityUnit quantityUnit;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(QuantityUnit quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public enum FoodType {
        UNIVERSAL_FOOD, CUSTOM_FOOD
    }

    public enum QuantityUnit {
        COUNT, CUP, GRAMS, ML, SLICE
    }

}
