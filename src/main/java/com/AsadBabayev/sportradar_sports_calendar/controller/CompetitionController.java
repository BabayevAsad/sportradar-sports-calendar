package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompetitionController {

    ResponseEntity<List<CompetitionDTO>> getAllCompetitions();

    ResponseEntity<CompetitionDTO> getCompetitionById(String id);

    ResponseEntity<CompetitionDTO> createCompetition(CompetitionRequestDTO requestDTO);

    ResponseEntity<CompetitionDTO> updateCompetition(CompetitionRequestDTO requestDTO, String id);

    ResponseEntity<Void> deleteCompetition(String id);
}
