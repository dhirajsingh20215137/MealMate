package com.malemate.demo.service;

import com.malemate.demo.Dao.MacroDao;
import com.malemate.demo.Dao.UserDao;
import com.malemate.demo.dto.MacroStatsDTO;
import com.malemate.demo.entity.User;
import com.malemate.demo.util.JwtUtil;
import com.malemate.demo.exceptions.BadRequestException;
import com.malemate.demo.exceptions.UnauthorizedException;
import com.malemate.demo.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class MacroService {

    private final MacroDao macroDao;
    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    public MacroService(MacroDao macroDao, JwtUtil jwtUtil, UserDao userDao) {
        this.macroDao = macroDao;
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    public MacroStatsDTO getMacrosStats(int userId, String macroType, String date, String token) {
        log.info("Fetching macro stats for userId: {}, macroType: {}, date: {}", userId, macroType, date);

        validateInput(macroType, date, token);

        String email = jwtUtil.extractEmail(token);
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        if (user.getUserId() != userId) {
            log.warn("Unauthorized access attempt by userId: {}", user.getUserId());
            throw new UnauthorizedException("Unauthorized action");
        }

        LocalDate targetDate = parseDate(date);

        float dailyTarget = macroDao.getUserMacroTarget(userId, macroType);
        float dailyAchieved = macroDao.getDailyAchievedMacro(userId, macroType, targetDate);
        float weeklyTarget = dailyTarget * 7;
        float weeklyAchieved = macroDao.getWeeklyAchievedMacro(userId, macroType, targetDate);
        float monthlyTarget = dailyTarget * targetDate.lengthOfMonth();
        float monthlyAchieved = macroDao.getMonthlyAchievedMacro(userId, macroType, targetDate);

        log.info("Macro stats computed successfully for userId: {}, macroType: {}", userId, macroType);

        return new MacroStatsDTO(dailyTarget, dailyAchieved, weeklyTarget, weeklyAchieved, monthlyTarget, monthlyAchieved);
    }

    private void validateInput(String macroType, String date, String token) {
        if (StringUtils.isBlank(token)) {
            log.error("Token is missing");
            throw new BadRequestException("Authentication token is required");
        }
        if (StringUtils.isBlank(macroType)) {
            log.error("Macro type is missing");
            throw new BadRequestException("Macro type is required");
        }
    }

    private LocalDate parseDate(String date) {
        try {
            return (StringUtils.isBlank(date)) ? LocalDate.now() : LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: {}", date);
            throw new BadRequestException("Invalid date format. Use YYYY-MM-DD.");
        }
    }
}
