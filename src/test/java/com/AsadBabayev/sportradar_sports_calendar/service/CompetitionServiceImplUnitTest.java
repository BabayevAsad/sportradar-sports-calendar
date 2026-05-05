package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Competition;
import com.AsadBabayev.sportradar_sports_calendar.mapper.CompetitionMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.CompetitionRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.CompetitionServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceImplUnitTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private CompetitionMapper competitionMapper;

    @InjectMocks
    private CompetitionServiceImpl competitionService;

    private Competition competition;
    private CompetitionDTO competitionDto;
    private CompetitionRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        competition = Competition.builder()
                .id(1L)
                .name("Premier League")
                .build();

        competitionDto = new CompetitionDTO();
        requestDto = new CompetitionRequestDTO();
    }

    @Test
    void shouldReturnAllCompetitions() {
        when(competitionRepository.findAll()).thenReturn(List.of(competition));
        when(competitionMapper.toDTO(competition)).thenReturn(competitionDto);

        List<CompetitionDTO> result = competitionService.getAllCompetitions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(competitionDto);
        verify(competitionRepository).findAll();
    }

    @Test
    void shouldReturnCompetitionByIdWhenIdExists() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        when(competitionMapper.toDTO(competition)).thenReturn(competitionDto);

        CompetitionDTO result = competitionService.getCompetitionById(1L);

        assertThat(result).isNotNull();
        verify(competitionRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCompetitionByIdNotFound() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> competitionService.getCompetitionById(1L));
    }

    @Test
    void shouldSaveCompetitionSuccessfully() {
        when(competitionRepository.save(any(Competition.class))).thenReturn(competition);
        when(competitionMapper.toDTO(competition)).thenReturn(competitionDto);

        CompetitionDTO result = competitionService.saveCompetition(requestDto);

        assertThat(result).isNotNull();
        verify(competitionMapper).updateEntityFromDto(eq(requestDto), any(Competition.class));
        verify(competitionRepository).save(any(Competition.class));
    }

    @Test
    void shouldUpdateCompetitionSuccessfully() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));
        when(competitionRepository.save(competition)).thenReturn(competition);
        when(competitionMapper.toDTO(competition)).thenReturn(competitionDto);

        CompetitionDTO result = competitionService.updateCompetition(requestDto, 1L);

        assertThat(result).isNotNull();
        verify(competitionMapper).updateEntityFromDto(requestDto, competition);
        verify(competitionRepository).save(competition);
    }

    @Test
    void shouldDeleteCompetitionSuccessfully() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.of(competition));

        competitionService.deleteCompetition(1L);

        verify(competitionRepository).delete(competition);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCompetition() {
        when(competitionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> competitionService.deleteCompetition(1L));
        verify(competitionRepository, never()).delete(any());
    }
}