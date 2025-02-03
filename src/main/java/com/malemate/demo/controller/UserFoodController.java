package com.malemate.demo.controller;

import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.service.UserFoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/{userId}/foods")
@Slf4j
public class UserFoodController {

    private final UserFoodService userFoodService;

    @Autowired
    public UserFoodController(UserFoodService userFoodService) {
        this.userFoodService = userFoodService;
    }

    @PostMapping
    public FoodResponseDTO addUserFood(@PathVariable int userId, @RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Adding food for user: {}, food name: {}", userId, foodDTO.getFoodName());

        FoodResponseDTO response = userFoodService.addUserFood(foodDTO, token, userId);

        log.info("Food added for user: {}, food name: {}", userId, foodDTO.getFoodName());
        return response;
    }

    @PostMapping("/{foodId}")
    public FoodResponseDTO updateUserFood(@PathVariable int userId, @PathVariable int foodId, @RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Updating food for user: {}, foodId: {}", userId, foodId);

        FoodResponseDTO response = userFoodService.updateUserFood(foodId, foodDTO, token, userId);

        log.info("Food updated for user: {}, foodId: {}", userId, foodId);
        return response;
    }

    @DeleteMapping("/{foodId}")
    public void deleteUserFood(@PathVariable int userId, @PathVariable int foodId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Deleting food for user: {}, foodId: {}", userId, foodId);

        userFoodService.deleteUserFood(foodId, token, userId);

        log.info("Food deleted for user: {}, foodId: {}", userId, foodId);
    }

    @GetMapping
    public List<FoodResponseDTO> getUserFoodItems(@PathVariable int userId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        log.info("Fetching food items for user: {}", userId);

        List<FoodResponseDTO> foodItems = userFoodService.getUserFoodItems(token, userId);

        log.info("Fetched food items for user: {}", userId);
        return foodItems;
    }
}
