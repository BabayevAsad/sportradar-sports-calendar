package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.StageController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.StageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/stages")
@RequiredArgsConstructor
public class StageControllerImpl implements StageController {

    private final StageService stageService;

    @GetMapping()
    @Override
    public ResponseEntity<List<StageDTO>> getAllStages() {
        return ResponseEntity.ok(stageService.getAllStages());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<StageDTO> getStageById(@PathVariable Long id) {
        return ResponseEntity.ok(stageService.getStageById(id));
    }

    @PostMapping()
    @Override
    public ResponseEntity<StageDTO> createStage(@Valid @RequestBody StageRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(stageService.saveStage(requestDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<StageDTO> updateStage(@Valid @RequestBody StageRequestDTO requestDTO,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(stageService.updateStage(requestDTO, id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteStage(@PathVariable Long id) {
        stageService.deleteStage(id);
        return ResponseEntity.noContent().build();
    }
}
