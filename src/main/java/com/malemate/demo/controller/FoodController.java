package com.malemate.demo.controller;

import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/admin/foods")
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public FoodResponseDTO addFood(@RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return foodService.addFood(foodDTO, token);
    }

    @PostMapping("/{foodId}")
    public FoodResponseDTO updateFood(@PathVariable int foodId,@RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return foodService.updateFood(foodId, foodDTO, token);
    }

    @DeleteMapping("/{foodId}")
    public void deleteFood(@PathVariable int foodId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        foodService.deleteFood(foodId, token);
    }

    // Get all food items
    @GetMapping
    public List<FoodResponseDTO> getAllFoodItems() {
        return foodService.getAllFoodItems();
    }
}
