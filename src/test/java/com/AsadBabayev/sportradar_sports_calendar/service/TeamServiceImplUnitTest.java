package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Team;
import com.AsadBabayev.sportradar_sports_calendar.mapper.TeamMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.TeamRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.TeamServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplUnitTest {

    @Mock private TeamRepository teamRepository;
    @Mock private TeamMapper teamMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;
    private TeamRequestDTO requestDTO;
    private TeamDTO teamDTO;

    @BeforeEach
    void setUp() {
        team = Team.builder()
                .id(1L)
                .name("Real Madrid")
                .slug("real-madrid")
                .build();

        requestDTO = TeamRequestDTO.builder()
                .name("Real Madrid")
                .slug("real-madrid")
                .build();

        teamDTO = TeamDTO.builder()
                .id(1L)
                .name("Real Madrid")
                .build();
    }

    @Test
    void shouldGetAllTeams() {
        when(teamRepository.findAll()).thenReturn(List.of(team));
        when(teamMapper.toDTO(team)).thenReturn(teamDTO);

        List<TeamDTO> result = teamService.getAllTeams();

        assertThat(result).hasSize(1);
        verify(teamRepository).findAll();
    }

    @Test
    void shouldGetTeamByIdSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMapper.toDTO(team)).thenReturn(teamDTO);

        TeamDTO result = teamService.getTeamById(1L);

        assertThat(result.getName()).isEqualTo("Real Madrid");
        verify(teamRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.getTeamById(1L));
    }

    @Test
    void shouldSaveTeamSuccessfully() {
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(teamMapper.toDTO(any(Team.class))).thenReturn(teamDTO);

        TeamDTO result = teamService.saveTeam(requestDTO);

        assertThat(result).isNotNull();
        verify(teamMapper).updateEntityFromDto(eq(requestDTO), any(Team.class));
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void shouldUpdateTeamSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        when(teamMapper.toDTO(any(Team.class))).thenReturn(teamDTO);

        TeamDTO result = teamService.updateTeam(requestDTO, 1L);

        assertThat(result).isNotNull();
        verify(teamRepository).findById(1L);
        verify(teamRepository).save(team);
    }

    @Test
    void shouldDeleteTeamSuccessfully() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        teamService.deleteTeam(1L);

        verify(teamRepository).delete(team);
    }

    @Test
    void shouldReturnEmptyListWhenNoTeamsExist() {
        when(teamRepository.findAll()).thenReturn(List.of());

        List<TeamDTO> result = teamService.getAllTeams();

        assertThat(result).isEmpty();
        verify(teamRepository).findAll();
        verify(teamMapper, never()).toDTO(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTeam() {
        Long nonExistentId = 99L;
        when(teamRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.updateTeam(requestDTO, nonExistentId));

        assertThat(exception.getMessage()).isEqualTo("Team not found with id: " + nonExistentId);
        verify(teamRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTeam() {
        Long nonExistentId = 99L;
        when(teamRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> teamService.deleteTeam(nonExistentId));

        verify(teamRepository, never()).delete(any());
    }
}