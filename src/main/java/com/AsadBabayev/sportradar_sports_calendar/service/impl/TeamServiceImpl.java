package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Team;
import com.AsadBabayev.sportradar_sports_calendar.mapper.TeamMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.TeamRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Transactional(readOnly = true)
    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TeamDTO getTeamById(Long id) {
        return teamMapper.toDTO(findByIdInternal(id));
    }

    @Transactional
    @Override
    public TeamDTO saveTeam(TeamRequestDTO requestDTO) {
        Team team = new Team();

        teamMapper.updateEntityFromDto(requestDTO, team);

        return teamMapper.toDTO(teamRepository.save(team));
    }

    @Transactional
    @Override
    public TeamDTO updateTeam(TeamRequestDTO requestDTO, Long id) {
        Team team = findByIdInternal(id);

        teamMapper.updateEntityFromDto(requestDTO, team);

        return teamMapper.toDTO(teamRepository.save(team));
    }

    @Transactional
    @Override
    public void deleteTeam(Long id) {
        Team team = findByIdInternal(id);

        teamRepository.delete(team);
    }

    private Team findByIdInternal(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    }
}
