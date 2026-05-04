package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;

import java.util.List;

public interface CompetitionService {

    List<CompetitionDTO> getAllCompetitions();

    CompetitionDTO getCompetitionById(Long id);

    CompetitionDTO saveCompetition(CompetitionRequestDTO competitionRequestDTO);

    CompetitionDTO updateCompetition(CompetitionRequestDTO competitionRequestDTO, Long id);

    void deleteCompetition(Long id);
}
