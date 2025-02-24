package com.malemate.demo.entity;
import jakarta.persistence.*;


@Entity
@Table(name = "meal_planner")
public class MealPlanner extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_planner_id")
    private int mealPlannerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "quantity_value", nullable = false)
    private float quantityValue;


    public int getMealPlannerId() {
        return mealPlannerId;
    }

    public void setMealPlannerId(int mealPlannerId) {
        this.mealPlannerId = mealPlannerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public float getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(float quantityValue) {
        this.quantityValue = quantityValue;
    }

    public enum MealType {
        BREAKFAST, LUNCH, DINNER, SNACK
    }
}
