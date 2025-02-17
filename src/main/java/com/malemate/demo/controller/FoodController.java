//package com.malemate.demo.controller;
//
//import com.malemate.demo.dto.FoodDTO;
//import com.malemate.demo.dto.FoodResponseDTO;
//import com.malemate.demo.service.FoodService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/user/admin/foods")
//@Slf4j
//public class FoodController {
//
//    private final FoodService foodService;
//
//    @Autowired
//    public FoodController(FoodService foodService) {
//        this.foodService = foodService;
//    }
//
//    @PostMapping
//    public FoodResponseDTO addFood(@RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
//        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
//        log.info("Adding food item: {}", foodDTO.getFoodName());
//
//        FoodResponseDTO response = foodService.addFood(foodDTO, token);
//        log.info("Food item added with ID: {}", response.getFoodId());
//        return response;
//    }
//
//    @PostMapping("/{foodId}")
//    public FoodResponseDTO updateFood(@PathVariable int foodId, @RequestBody FoodDTO foodDTO, @RequestHeader("Authorization") String authorizationHeader) {
//        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
//        log.info("Updating food item with ID: {}", foodId);
//
//        FoodResponseDTO response = foodService.updateFood(foodId, foodDTO, token);
//        log.info("Food item with ID: {} updated successfully", foodId);
//        return response;
//    }
//
//    @DeleteMapping("/{foodId}")
//    public void deleteFood(@PathVariable int foodId, @RequestHeader("Authorization") String authorizationHeader) {
//        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
//        log.info("Deleting food item with ID: {}", foodId);
//
//        foodService.deleteFood(foodId, token);
//        log.info("Food item with ID: {} deleted successfully", foodId);
//    }
//
//
//    @GetMapping
//    public List<FoodResponseDTO> getAllFoodItems() {
//        log.info("Fetching all food items");
//        List<FoodResponseDTO> foodItems = foodService.getAllFoodItems();
//        log.info("Fetched {} food items", foodItems.size());
//        return foodItems;
//    }
//}
