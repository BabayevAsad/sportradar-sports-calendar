package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StageController {

    ResponseEntity<List<StageDTO>> getAllStages();

    ResponseEntity<StageDTO> getStageById(Long id);

    ResponseEntity<StageDTO> createStage(StageRequestDTO requestDTO);

    ResponseEntity<StageDTO> updateStage(StageRequestDTO requestDTO, Long id);

    ResponseEntity<Void> deleteStage(Long id);
}
