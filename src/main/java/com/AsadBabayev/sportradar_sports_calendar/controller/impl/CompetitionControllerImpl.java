package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.CompetitionController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/competitions")
@RequiredArgsConstructor
public class CompetitionControllerImpl implements CompetitionController {

    private final CompetitionService competitionService;

    @GetMapping
    @Override
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions() {
        return ResponseEntity.ok(competitionService.getAllCompetitions());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        return ResponseEntity.ok(competitionService.getCompetitionById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @RequestBody CompetitionRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(competitionService.saveCompetition(requestDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<CompetitionDTO> updateCompetition(@Valid @RequestBody CompetitionRequestDTO requestDTO,
                                                            @PathVariable Long id) {
        return ResponseEntity.ok(competitionService.updateCompetition(requestDTO, id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
        return ResponseEntity.noContent().build();
    }
}
