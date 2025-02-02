package com.malemate.demo.service;

import com.malemate.demo.Dao.MacroDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.MacroStatsDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MacroService {

    private final MacroDao macroDao;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    public MacroService(MacroDao macroDao, JwtUtil jwtUtil,UserDao userDao) {
        this.macroDao = macroDao;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    public MacroStatsDTO getMacrosStats(int userId, String macroType, String date, String token) {

        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserId() != userId) {
            throw new RuntimeException("Unauthorized action");
        }


//        if (!jwtUtil.isTokenValid(token, userId)) {
//            throw new IllegalArgumentException("Invalid or expired token");
//        }


        LocalDate targetDate = (date == null) ? LocalDate.now() : LocalDate.parse(date);


        float dailyTarget = macroDao.getUserMacroTarget(userId, macroType);
        float dailyAchieved = macroDao.getDailyAchievedMacro(userId, macroType, targetDate);


        float weeklyTarget = dailyTarget * 7;
        float weeklyAchieved = macroDao.getWeeklyAchievedMacro(userId, macroType, targetDate);


        float monthlyTarget = dailyTarget * targetDate.lengthOfMonth();
        float monthlyAchieved = macroDao.getMonthlyAchievedMacro(userId, macroType, targetDate);


        return new MacroStatsDTO(dailyTarget, dailyAchieved, weeklyTarget, weeklyAchieved, monthlyTarget, monthlyAchieved);
    }
}
