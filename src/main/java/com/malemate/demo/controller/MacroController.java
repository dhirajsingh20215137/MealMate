package com.malemate.demo.controller;

import com.malemate.demo.dto.MacroStatsDTO;
import com.malemate.demo.service.MacroService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/user/{userId}/stats")
@Log4j2
public class MacroController {

    private final MacroService macroService;

    public MacroController(MacroService macroService) {
        this.macroService = macroService;
    }

    @GetMapping("/carbs")
    public MacroStatsDTO getCarbsStats(@PathVariable int userId,
                                       @RequestHeader("Authorization") String authorizationToken)
    {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching carbs stats for user: {} on date: {}", userId, "latest");
        MacroStatsDTO response = macroService.getMacrosStats(userId, "carbs",token);
        log.info("Carbs stats fetched for user: {}", userId);
        return response;
    }

    @GetMapping("/proteins")
    public MacroStatsDTO getProteinStats(@PathVariable int userId,
                                         @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching protein stats for user: {} ", userId);
        MacroStatsDTO response = macroService.getMacrosStats(userId, "proteins",  token);
        log.info("Protein stats fetched for user: {}", userId);
        return response;
    }

    @GetMapping("/fats")
    public MacroStatsDTO getFatsStats(@PathVariable int userId,
                                          @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        log.info("Fetching fats stats for user: {} ", userId);
        MacroStatsDTO response = macroService.getMacrosStats(userId, "fats", token);
        log.info("Fats stats fetched for user: {}", userId);
        return response;
    }
}
