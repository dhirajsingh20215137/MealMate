package com.malemate.demo.service;

import com.malemate.demo.Dao.FoodDao;
import com.malemate.demo.Dao.MealPlannerDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.MealPlannerRequestDTO;
import com.malemate.demo.dto.MealPlannerResponseDTO;
import com.malemate.demo.entity.Food;
import com.malemate.demo.entity.MealPlanner;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealPlannerService {

    private final MealPlannerDao mealPlannerDao;
    private final FoodDao foodDao;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public MealPlannerService(MealPlannerDao mealPlannerDao, FoodDao foodDao, UserDao userDao, JwtUtil jwtUtil) {
        this.mealPlannerDao = mealPlannerDao;
        this.foodDao = foodDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
    }


    public MealPlannerResponseDTO addFoodToMealPlan(int userId, MealPlannerRequestDTO requestDTO, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("Unauthorized action");
        }


        Food food = foodDao.findById(requestDTO.getFoodId())
                .orElseThrow(() -> new RuntimeException("Food not found"));


        if (food.getFoodType() == Food.FoodType.CUSTOM_FOOD) {
            if (food.getUser() == null || food.getUser().getUserId() != userId) {
                throw new RuntimeException("You cannot add another user's custom food");
            }
        }

        MealPlanner mealPlanner = new MealPlanner();
        mealPlanner.setUser(user);
        mealPlanner.setFood(food);
        mealPlanner.setMealType(MealPlanner.MealType.valueOf(requestDTO.getMealType().toUpperCase()));
        mealPlanner.setQuantityValue(requestDTO.getQuantityValue());

        mealPlannerDao.save(mealPlanner);

        return mapToMealPlannerResponseDTO(mealPlanner);
    }


    public String removeFoodFromMealPlan(int userId, int foodId, String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("Unauthorized action");
        }

        mealPlannerDao.deleteByUserIdAndFoodId(userId, foodId);
        return "Food removed from meal plan";
    }


    public List<MealPlannerResponseDTO> getUserMealPlan(int userId) {
        List<MealPlanner> mealPlans = mealPlannerDao.findByUserId(userId);
        return mealPlans.stream().map(this::mapToMealPlannerResponseDTO).collect(Collectors.toList());
    }

    private MealPlannerResponseDTO mapToMealPlannerResponseDTO(MealPlanner mealPlanner) {
        return new MealPlannerResponseDTO(
                mealPlanner.getMealPlannerId(),
                mealPlanner.getUser().getUserId(),
                mealPlanner.getFood().getFoodId(),
                mealPlanner.getFood().getFoodName(),
                mealPlanner.getMealType().name(),
                mealPlanner.getQuantityValue()
        );
    }
}
