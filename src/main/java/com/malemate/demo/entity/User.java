package com.malemate.demo.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "weight")
    private float weight;

    @Column(name = "height")
    private float height;

    @Column(name = "targeted_carbs")
    private float targetedCarbs;

    @Column(name = "targeted_protein")
    private float targetedProtein;

    @Column(name = "targeted_calories")
    private float targetedCalories;

    @Enumerated(EnumType.STRING)
    @Column(name="user_type")
    private UserType userType=UserType.USER;

    @Column(name = "user_url")
    private  String userUrl;




    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getTargetedCarbs() {
        return targetedCarbs;
    }

    public void setTargetedCarbs(float targetedCarbs) {
        this.targetedCarbs = targetedCarbs;
    }

    public float getTargetedProtein() {
        return targetedProtein;
    }

    public void setTargetedProtein(float targetedProtein) {
        this.targetedProtein = targetedProtein;
    }

    public float getTargetedCalories() {
        return targetedCalories;
    }

    public void setTargetedCalories(float targetedCalories) {
        this.targetedCalories = targetedCalories;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
    public enum UserType {

        ADMIN, USER
    }

}
