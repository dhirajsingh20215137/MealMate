package com.malemate.demo.dto;
import lombok.Data;



public class SignupRequestDTO {
    private String email;
    private String password;
    private String gender;
    private float weight;
    private float height;
    private float targetedCarbs;
    private float targetedProtein;
    private float targetedCalories;

    // Getters and setters
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

}
