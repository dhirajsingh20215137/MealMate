package com.malemate.demo.dto;

import com.malemate.demo.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserProfileDTO {

    private int userId;
    //private String email;
    private User.Gender gender;
    private float weight;
    private float height;
    private float targetedCarbs;
    private float targetedProtein;
    private float targetedCalories;
    private String UserUrl;
   // private User.UserType userType;



}
