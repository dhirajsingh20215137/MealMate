package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.FoodDTO;
import com.malemate.demo.dto.FoodResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.User;
import com.malemate.demo.exceptions.*;
import com.malemate.demo.exceptions.BadRequestException;
import com.malemate.demo.exceptions.ResourceNotFoundException;
import com.malemate.demo.exceptions.UnauthorizedException;
import com.malemate.demo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class UserFoodService {

    private final FoodDao foodDao;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    public UserFoodService(FoodDao foodDao, UserDao userDao, JwtUtil jwtUtil) {
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }

    public FoodResponseDTO addUserFood(FoodDTO foodDTO, String token, int userId, MultipartFile file) {
        log.info("Adding food to user collection for userId: {}", userId);

        // Validate user and food request
        validateUserFoodRequest(foodDTO, token, userId);

        // Authenticate user
        User user = getAuthenticatedUser(userId, token);
        log.info("Authenticated user: {}", userId);

        // Create Food entity
        Food food = new Food();
        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));
        food.setUser(user);

        // Handle photo upload if a file is provided
        if (file != null && !file.isEmpty()) {
            log.info("Uploading photo for food: {}", foodDTO.getFoodName());

            // Ensure the upload directory exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                    log.info("Created upload directory: {}", uploadPath.toString());
                } catch (IOException e) {
                    log.error("Error creating upload directory: {}", e.getMessage(), e);
                }
            }

            // Generate a unique filename for the uploaded photo
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                log.error("Uploaded file has no name");
                return null; // Handle accordingly (e.g., return an error response)
            }

            // Get file extension and create unique filename
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String uniqueFilename = baseName + "_" + System.currentTimeMillis() + fileExtension;

            // Define the path where the file will be saved
            Path filePath = uploadPath.resolve(uniqueFilename);
            log.info("Saving file to: {}", filePath.toString());

            try {
                // Save the file to the server
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                log.info("Food image saved with filename: {}", uniqueFilename);
            } catch (IOException e) {
                log.error("Error saving file: {}", e.getMessage(), e);
                return null; // Handle accordingly (e.g., return an error response)
            }

            // Set the image URL (filename) in the food object
            food.setImageUrl(uniqueFilename);
        } else {
            // If no file is provided, use the filename from the foodDTO
            food.setImageUrl(foodDTO.getImageUrl());
            log.info("No image file uploaded. Using provided image URL: {}", foodDTO.getImageUrl());
        }

        // Save the food object in the database
        foodDao.save(food);
        log.info("Food added successfully for userId: {}", userId);

        return mapToFoodResponseDTO(food);
    }


    public Food uploadFoodImage(MultipartFile file, int userId) throws IOException {
        log.info("Uploading food image for userId: {}", userId);

        // Fetch user (Ensure user exists and is not deleted)
        User user = userDao.getUserById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found or marked as deleted"));

        // Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Get the original file name and create a unique name
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String uniqueFilename = baseName + "_" + System.currentTimeMillis() + fileExtension;

        // Define the path where the file will be saved
        Path filePath = uploadPath.resolve(uniqueFilename);

        try {
            // Copy the file to the destination
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }

        // Create the food object and set the image URL
        Food food = new Food();
        food.setUser(user); // Associate the food with the user
        food.setImageUrl(uniqueFilename); // Save only the filename (e.g., abc123_image.jpg)

        // Save the food object to the database
        foodDao.save(food);

        log.info("Food image uploaded successfully for userId: {}", userId);
        return food; // Return the food object with the image URL
    }






    public FoodResponseDTO updateUserFood(int foodId, FoodDTO foodDTO, String token, int userId) {
        log.info("Updating food for userId: {}, foodId: {}", userId, foodId);

        validateUserFoodRequest(foodDTO, token, userId);

        User user = getAuthenticatedUser(userId, token);

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> {
                    log.error("Food item not found for foodId: {}", foodId);
                    return new ResourceNotFoundException("Food item not found");
                });

        if (!food.getUser().equals(user)) {
            log.warn("Unauthorized food update attempt for userId: {}", userId);
            throw new UnauthorizedException("You can only update your own food items");
        }

        food.setFoodName(foodDTO.getFoodName());
        food.setCalories(foodDTO.getCalories());
        food.setProteins(foodDTO.getProteins());
        food.setCarbs(foodDTO.getCarbs());
        food.setFoodType(Food.FoodType.valueOf(foodDTO.getFoodType()));
        food.setQuantityUnit(Food.QuantityUnit.valueOf(foodDTO.getQuantityUnit()));

        // Directly set the updated image filename
        food.setImageUrl(foodDTO.getImageUrl());

        foodDao.save(food);
        log.info("Food updated successfully for userId: {}", userId);

        return mapToFoodResponseDTO(food);
    }


    public void deleteUserFood(int foodId, String token, int userId) {
        log.info("Deleting food for userId: {}, foodId: {}", userId, foodId);

        User user = getAuthenticatedUser(userId, token);

        Food food = foodDao.findById(foodId)
                .orElseThrow(() -> {
                    log.error("Food item not found for foodId: {}", foodId);
                    return new ResourceNotFoundException("Food item not found");
                });

        if (!food.getUser().equals(user)) {
            log.warn("Unauthorized food delete attempt for userId: {}", userId);
            throw new UnauthorizedException("You can only delete your own food items");
        }


        food.setDeleted(true);
        foodDao.save(food);
        log.info("Food deleted successfully for userId: {}", userId);
    }

    public List<FoodResponseDTO> getUserFoodItems(String token, int userId) {
        log.info("Fetching food items for userId: {}", userId);


        User user = getAuthenticatedUser(userId, token);

        List<Food> allFoods = Stream.concat(
                        foodDao.getFoodItemsByType(Food.FoodType.UNIVERSAL_FOOD).stream(),
                        foodDao.getFoodItemsByUserId(user.getUserId()).stream()
                )
//                .filter(food -> !food.isDeleted())
                .toList();

        return allFoods.stream().map(this::mapToFoodResponseDTO).toList();
    }

    private FoodResponseDTO mapToFoodResponseDTO(Food food) {
        return FoodResponseDTO.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .calories(food.getCalories())
                .proteins(food.getProteins())
                .carbs(food.getCarbs())
                .quantityUnit(food.getQuantityUnit().name())
                .foodType(food.getFoodType().name())
                .imageUrl(food.getImageUrl())
                .build();
    }

    private User getAuthenticatedUser(int userId, String token) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new SecurityException("Authentication token is required");
        }

        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        if (user.getUserId() != userId) {
            log.warn("Unauthorized access attempt by userId: {}", userId);
            throw new UnauthorizedException("Unauthorized action");
        }

        log.info("User authenticated successfully for userId: {}", userId);

        return user;
    }

    private void validateUserFoodRequest(FoodDTO foodDTO, String token, int userId) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new BadRequestException("Authentication token is required");
        }
        if (foodDTO == null) {
            log.error("FoodDTO is null");
            throw new BadRequestException("Food details cannot be null");
        }
        if (StringUtils.isBlank(foodDTO.getFoodName())) {
            log.error("Food name is missing");
            throw new BadRequestException("Food name is required");
        }
        if (foodDTO.getCalories() <= 0) {
            log.error("Invalid calories value: {}", foodDTO.getCalories());
            throw new BadRequestException("Calories must be greater than zero");
        }
        if (foodDTO.getProteins() <= 0) {
            log.error("Invalid proteins value: {}", foodDTO.getProteins());
            throw new BadRequestException("Proteins must be greater than zero");
        }
        if (foodDTO.getCarbs() <= 0) {
            log.error("Invalid carbs value: {}", foodDTO.getCarbs());
            throw new BadRequestException("Carbs must be greater than zero");
        }
        if (StringUtils.isBlank(foodDTO.getFoodType())) {
            log.error("Food type is missing");
            throw new BadRequestException("Food type is required");
        }
        if (StringUtils.isBlank(foodDTO.getQuantityUnit())) {
            log.error("Quantity unit is missing");
            throw new BadRequestException("Quantity unit is required");
        }

        log.info("validation successfull");
    }
}
