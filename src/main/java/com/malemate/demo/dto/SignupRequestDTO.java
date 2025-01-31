package com.malemate.demo.dto;
import com.malemate.demo.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignupRequestDTO {
    private String email;
    private String password;
    private User.UserType userType;
//    private String gender;
//    private float weight;
//    private float height;
//    private float targetedCarbs;
//    private float targetedProtein;
//    private float targetedCalories;



}
