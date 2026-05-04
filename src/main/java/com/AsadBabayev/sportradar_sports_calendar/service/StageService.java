package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;

import java.util.List;

public interface StageService {

    List<StageDTO> getAllStages();

    StageDTO getStageById(Long id);

    StageDTO saveStage(StageRequestDTO stageRequestDTO);

    StageDTO updateStage(StageRequestDTO stageRequestDTO, Long id);

    void deleteStage(Long id);
}
