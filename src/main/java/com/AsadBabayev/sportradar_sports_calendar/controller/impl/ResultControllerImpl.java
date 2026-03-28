package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.ResultController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.ResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/results")
@RequiredArgsConstructor
public class ResultControllerImpl implements ResultController {

    private final ResultService resultService;

    @GetMapping()
    @Override
    public ResponseEntity<List<ResultDTO>> getAllResults() {
        return ResponseEntity.ok(resultService.getAllResults());
    }

    @GetMapping("/{eventId}")
    @Override
    public ResponseEntity<ResultDTO> getResultByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(resultService.getResultByEventId(eventId));
    }

    @PostMapping()
    @Override
    public ResponseEntity<ResultDTO> createResult(@Valid @RequestBody ResultRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(resultService.saveResult(requestDTO));
    }

    @PutMapping("/{eventId}")
    @Override
    public ResponseEntity<ResultDTO> updateResult(@Valid @RequestBody ResultRequestDTO requestDTO,
                                                  @PathVariable Long eventId) {
        return ResponseEntity.ok(resultService.updateResult(requestDTO, eventId));
    }

    @DeleteMapping("/{eventId}")
    @Override
    public ResponseEntity<Void> deleteResult(@PathVariable Long eventId) {
        resultService.deleteResult(eventId);
        return ResponseEntity.noContent().build();
    }
}
