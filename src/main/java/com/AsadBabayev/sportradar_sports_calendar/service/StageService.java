package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;

import java.util.List;

public interface StageService {

    List<StageDTO> getAllStages();

    StageDTO getStageById(String id);

    StageDTO saveStage(StageRequestDTO stageRequestDTO);

    StageDTO updateStage(StageRequestDTO stageRequestDTO, String id);

    void deleteStage(String id);
}
