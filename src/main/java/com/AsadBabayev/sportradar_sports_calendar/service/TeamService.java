package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;

import java.util.List;

public interface TeamService {

    List<TeamDTO> getAllTeams();

    TeamDTO getTeamById(Long id);

    TeamDTO saveTeam(TeamRequestDTO teamRequestDTO);

    TeamDTO updateTeam(TeamRequestDTO teamRequestDTO, Long id);

    void deleteTeam(Long id);
}
