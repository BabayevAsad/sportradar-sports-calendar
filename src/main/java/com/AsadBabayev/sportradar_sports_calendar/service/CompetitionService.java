package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;

import java.util.List;

public interface CompetitionService {

    List<CompetitionDTO> getAllCompetitions();

    CompetitionDTO getCompetitionById(String id);

    CompetitionDTO saveCompetition(CompetitionRequestDTO competitionRequestDTO);

    CompetitionDTO updateCompetition(CompetitionRequestDTO competitionRequestDTO, String id);

    void deleteCompetition(String id);
}
