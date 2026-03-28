package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamController {

    ResponseEntity<List<TeamDTO>> getAllTeams();

    ResponseEntity<TeamDTO> getTeamById(Long id);

    ResponseEntity<TeamDTO> createTeam(TeamRequestDTO requestDTO);

    ResponseEntity<TeamDTO> updateTeam(TeamRequestDTO requestDTO, Long id);

    ResponseEntity<Void> deleteTeam(Long id);
}
