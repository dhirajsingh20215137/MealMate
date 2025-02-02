package com.malemate.demo.controller;

import com.malemate.demo.dto.MacroStatsDTO;
import com.malemate.demo.service.MacroService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/stats")
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
        return macroService.getMacrosStats(userId, "carbs", date, token);
    }

    @GetMapping("/proteins")
    public MacroStatsDTO getProteinStats(@PathVariable int userId,
                                         @RequestParam(required = false) String date,
                                         @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        return macroService.getMacrosStats(userId, "proteins", date, token);
    }

    @GetMapping("/calories")
    public MacroStatsDTO getCaloriesStats(@PathVariable int userId,
                                          @RequestParam(required = false) String date,
                                          @RequestHeader("Authorization") String authorizationToken) {
        String token = authorizationToken.startsWith("Bearer ") ? authorizationToken.substring(7) : authorizationToken;
        return macroService.getMacrosStats(userId, "calories", date, token);
    }
}
