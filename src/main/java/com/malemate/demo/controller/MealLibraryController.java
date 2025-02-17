package com.malemate.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.service.UserFoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user/{userId}/foods")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class MealLibraryController {

    private final UserFoodService userFoodService;

    @Autowired
    public MealLibraryController(UserFoodService userFoodService) {
        this.userFoodService = userFoodService;
    }

    @PostMapping
    public List<FoodResponseDTO> addUserFood(
            @PathVariable int userId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("foodDTO") String foodDTOJson,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        FoodDTO foodDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            foodDTO = objectMapper.readValue(foodDTOJson, FoodDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing foodDTO JSON: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid foodDTO data", e);
        }

        log.info("Adding food for user: {}, food name: {}", userId, foodDTO.getFoodName());
        userFoodService.addUserFood(foodDTO, token, userId, file);
        log.info("Food added for user: {}", userId);
        List<FoodResponseDTO> foodItems = userFoodService.getUserFoodItems(token, userId);
        log.info("Fetched {} food items for user: {}", foodItems.size(), userId);
        return foodItems;
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
        log.info("Fetching food items for user with its FoodId:  {}", userId);
        List<FoodResponseDTO> foodItems = userFoodService.getUserFoodItems(token, userId);
        log.info("Fetched food items for user with their  FoodId: {}", userId);
        return foodItems;
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFoodImage(
            @PathVariable int userId,
            @RequestParam("file") MultipartFile file) {
        log.info("Received photo upload request for userId: {}", userId);
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            Food food = userFoodService.uploadFoodImage(file, userId);
            return ResponseEntity.ok().body(Map.of("filename", food.getImageUrl()));
        } catch (IOException e) {
            log.error("Failed to upload food image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}
