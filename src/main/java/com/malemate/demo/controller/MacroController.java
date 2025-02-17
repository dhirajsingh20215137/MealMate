package com.malemate.demo.controller;

import com.malemate.demo.dto.MacroStatsDTO;
import com.malemate.demo.service.MacroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/stats")
@Slf4j
public class MacroController {

    private final MacroService macroService;

    public MacroController(MacroService macroService) {
        this.macroService = macroService;
    }

    @GetMapping("/carbs")
    public MacroStatsDTO getCarbsStats(@PathVariable int userId,
                                       @RequestParam(required = false) String date,
                                       @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching carbs stats for user: {} on date: {}", userId, date != null ? date : "latest");
        MacroStatsDTO response = macroService.getMacrosStats(userId, "carbs", date, token);
        log.info("Carbs stats fetched for user: {}", userId);
        return response;
    }

    @GetMapping("/proteins")
    public MacroStatsDTO getProteinStats(@PathVariable int userId,
                                         @RequestParam(required = false) String date,
                                         @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching protein stats for user: {} on date: {}", userId, date != null ? date : "latest");
        MacroStatsDTO response = macroService.getMacrosStats(userId, "proteins", date, token);
        log.info("Protein stats fetched for user: {}", userId);
        return response;
    }

    @GetMapping("/calories")
    public MacroStatsDTO getCaloriesStats(@PathVariable int userId,
                                          @RequestParam(required = false) String date,
                                          @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching calories stats for user: {} on date: {}", userId, date != null ? date : "latest");
        MacroStatsDTO response = macroService.getMacrosStats(userId, "calories", date, token);
        log.info("Calories stats fetched for user: {}", userId);
        return response;
    }
}
