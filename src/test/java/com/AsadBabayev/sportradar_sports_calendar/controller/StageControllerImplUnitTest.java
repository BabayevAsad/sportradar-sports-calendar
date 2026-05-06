package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.StageControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.exception.GlobalExceptionHandler;
import com.AsadBabayev.sportradar_sports_calendar.service.StageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StageControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private StageService stageService;

    @InjectMocks
    private StageControllerImpl stageController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/stages";

    private StageDTO stageDto;
    private StageRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        stageDto = new StageDTO();
        stageDto.setId(1L);
        stageDto.setName("Regular Season");

        requestDto = new StageRequestDTO();
        requestDto.setName("Regular Season");
    }

    @Test
    void shouldReturnAllStages() throws Exception {
        when(stageService.getAllStages()).thenReturn(List.of(stageDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Regular Season"));
    }

    @Test
    void shouldReturnStageById() throws Exception {
        when(stageService.getStageById(1L)).thenReturn(stageDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Regular Season"));
    }

    @Test
    void shouldCreateStageAndReturn201() throws Exception {
        when(stageService.saveStage(any(StageRequestDTO.class))).thenReturn(stageDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Regular Season"));
    }

    @Test
    void shouldUpdateStageSuccessfully() throws Exception {
        when(stageService.updateStage(any(StageRequestDTO.class), eq(1L))).thenReturn(stageDto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteStageSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(stageService).deleteStage(1L);
    }

    @Test
    void shouldReturnNotFoundWhenStageDoesNotExist() throws Exception {
        when(stageService.getStageById(99L))
                .thenThrow(new EntityNotFoundException("Stage not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }
}
