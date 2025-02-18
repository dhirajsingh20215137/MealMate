package com.malemate.demo.service.impl;



import com.malemate.demo.dto.MacroStatsDTO;

public interface MacroServiceInterface {
    MacroStatsDTO getMacrosStats(int userId, String macroType,  String token);
}
