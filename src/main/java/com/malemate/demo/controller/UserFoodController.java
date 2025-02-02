package com.malemate.demo.controller;

import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.service.UserFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/foods")
public class UserFoodController {

    private final UserFoodService userFoodService;

    @Autowired
    public UserFoodController(UserFoodService userFoodService) {
        this.userFoodService = userFoodService;
    }

    @PostMapping
    public FoodResponseDTO addUserFood(@PathVariable int userId,@RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return userFoodService.addUserFood(foodDTO, token,userId);
    }

    @PostMapping("/{foodId}")
    public FoodResponseDTO updateUserFood(@PathVariable int userId,@PathVariable int foodId, @RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return userFoodService.updateUserFood(foodId, foodDTO, token,userId);
    }

    @DeleteMapping("/{foodId}")
    public void deleteUserFood(@PathVariable int userId,@PathVariable int foodId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        userFoodService.deleteUserFood(foodId, token,userId);
    }




    @GetMapping
    public List<FoodResponseDTO> getUserFoodItems(@PathVariable int userId,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return userFoodService.getUserFoodItems(token, userId);
    }
}
