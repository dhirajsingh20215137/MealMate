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

    public enum FoodType {
        UNIVERSAL_FOOD, CUSTOM_FOOD
    }

    public enum QuantityUnit {
        COUNT, CUP, GRAMS, ML, SLICE
    }

}
