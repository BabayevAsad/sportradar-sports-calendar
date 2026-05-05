package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Stage;
import com.AsadBabayev.sportradar_sports_calendar.mapper.StageMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.StageRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.StageServiceImpl;
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
class StageServiceImplUnitTest {

    @Mock
    private StageRepository stageRepository;

    @Mock
    private StageMapper stageMapper;

    @InjectMocks
    private StageServiceImpl stageService;

    private Stage stage;
    private StageDTO stageDto;
    private StageRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        stage = Stage.builder()
                .id(1L)
                .name("Group Stage")
                .build();

        stageDto = new StageDTO();
        requestDto = new StageRequestDTO();
    }

    @Test
    void shouldReturnAllStages() {
        when(stageRepository.findAll()).thenReturn(List.of(stage));
        when(stageMapper.toDTO(stage)).thenReturn(stageDto);

        List<StageDTO> result = stageService.getAllStages();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(stageDto);
        verify(stageRepository).findAll();
    }

    @Test
    void shouldReturnStageByIdWhenIdExists() {
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(stageMapper.toDTO(stage)).thenReturn(stageDto);

        StageDTO result = stageService.getStageById(1L);

        assertThat(result).isNotNull();
        verify(stageRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenStageByIdNotFound() {
        when(stageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stageService.getStageById(1L));
    }

    @Test
    void shouldSaveStageSuccessfully() {
        when(stageRepository.save(any(Stage.class))).thenReturn(stage);
        when(stageMapper.toDTO(stage)).thenReturn(stageDto);

        StageDTO result = stageService.saveStage(requestDto);

        assertThat(result).isNotNull();
        verify(stageMapper).updateEntityFromDto(eq(requestDto), any(Stage.class));
        verify(stageRepository).save(any(Stage.class));
    }

    @Test
    void shouldUpdateStageSuccessfully() {
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));
        when(stageRepository.save(stage)).thenReturn(stage);
        when(stageMapper.toDTO(stage)).thenReturn(stageDto);

        StageDTO result = stageService.updateStage(requestDto, 1L);

        assertThat(result).isNotNull();
        verify(stageMapper).updateEntityFromDto(requestDto, stage);
        verify(stageRepository).save(stage);
    }

    @Test
    void shouldDeleteStageSuccessfully() {
        when(stageRepository.findById(1L)).thenReturn(Optional.of(stage));

        stageService.deleteStage(1L);

        verify(stageRepository).delete(stage);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentStage() {
        when(stageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> stageService.deleteStage(1L));
        verify(stageRepository, never()).delete(any());
    }
}