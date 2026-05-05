package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.mapper.SportMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.SportRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.SportServiceImpl;
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
class SportServiceImplUnitTest {

    @Mock
    private SportRepository sportRepository;

    @Mock
    private SportMapper sportMapper;

    @InjectMocks
    private SportServiceImpl sportService;

    private Sport sport;
    private SportDto sportDto;
    private SportRequestDto requestDto;

    @BeforeEach
    void setUp() {
        sport = Sport.builder()
                .id(1L)
                .name(SportType.FOOTBALL)
                .build();

        sportDto = new SportDto();

        requestDto = new SportRequestDto();
        requestDto.setName(SportType.FOOTBALL);
    }

    @Test
    void shouldReturnAllSports() {
        when(sportRepository.findAll()).thenReturn(List.of(sport));
        when(sportMapper.toDTO(sport)).thenReturn(sportDto);

        List<SportDto> result = sportService.getAllSports();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(sportDto);
        verify(sportRepository).findAll();
    }

    @Test
    void shouldReturnSportByIdWhenIdExists() {
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(sportMapper.toDTO(sport)).thenReturn(sportDto);

        SportDto result = sportService.getSportById(1L);

        assertThat(result).isNotNull();
        verify(sportRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenSportByIdNotFound() {
        when(sportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sportService.getSportById(1L));
    }

    @Test
    void shouldSaveSportSuccessfully() {
        when(sportRepository.existsByName(requestDto.getName())).thenReturn(false);
        when(sportRepository.save(any(Sport.class))).thenReturn(sport);
        when(sportMapper.toDTO(sport)).thenReturn(sportDto);

        SportDto result = sportService.saveSport(requestDto);

        assertThat(result).isNotNull();
        verify(sportMapper).updateEntityFromDto(eq(requestDto), any(Sport.class));
        verify(sportRepository).save(any(Sport.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingSportWithExistingName() {
        when(sportRepository.existsByName(requestDto.getName())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sportService.saveSport(requestDto));

        assertThat(exception.getMessage()).contains("Sport category already exists");
        verify(sportRepository, never()).save(any());
    }

    @Test
    void shouldUpdateSportSuccessfully() {
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));
        when(sportRepository.save(sport)).thenReturn(sport);
        when(sportMapper.toDTO(sport)).thenReturn(sportDto);

        SportDto result = sportService.updateSport(requestDto, 1L);

        assertThat(result).isNotNull();
        verify(sportMapper).updateEntityFromDto(requestDto, sport);
        verify(sportRepository).save(sport);
    }

    @Test
    void shouldDeleteSportSuccessfully() {
        when(sportRepository.findById(1L)).thenReturn(Optional.of(sport));

        sportService.deleteSport(1L);

        verify(sportRepository).delete(sport);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentSport() {
        when(sportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sportService.deleteSport(1L));
        verify(sportRepository, never()).delete(any());
    }
}