package com.malemate.demo.dto;

import com.malemate.demo.entity.User;

public class UserProfileDTO {

    private int userId;
    private String email;
    private User.Gender gender;
    private float weight;
    private float height;
    private float targetedCarbs;
    private float targetedProtein;
    private float targetedCalories;
    private String UserUrl;
    private User.UserType userType;
    // Getters and Setters


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

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
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

    public String getUserUrl() {
        return UserUrl;
    }

    public void setUserUrl(String userUrl) {
        UserUrl = userUrl;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }
}
