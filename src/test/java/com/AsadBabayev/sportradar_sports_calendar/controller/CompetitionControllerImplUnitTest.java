package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.CompetitionControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.exception.GlobalExceptionHandler;
import com.AsadBabayev.sportradar_sports_calendar.service.CompetitionService;
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
class CompetitionControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private CompetitionControllerImpl competitionController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/competitions";

    private CompetitionDTO competitionDto;
    private CompetitionRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(competitionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        competitionDto = new CompetitionDTO();
        competitionDto.setId(1L);
        competitionDto.setName("Premier League");

        requestDto = new CompetitionRequestDTO();
        requestDto.setName("Premier League");
        requestDto.setSportId(1L);
    }

    @Test
    void shouldReturnAllCompetitions() throws Exception {
        when(competitionService.getAllCompetitions()).thenReturn(List.of(competitionDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Premier League"));
    }

    @Test
    void shouldReturnCompetitionById() throws Exception {
        when(competitionService.getCompetitionById(1L)).thenReturn(competitionDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Premier League"));
    }

    @Test
    void shouldCreateCompetitionAndReturn201() throws Exception {
        when(competitionService.saveCompetition(any(CompetitionRequestDTO.class))).thenReturn(competitionDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Premier League"));
    }

    @Test
    void shouldUpdateCompetitionSuccessfully() throws Exception {
        when(competitionService.updateCompetition(any(CompetitionRequestDTO.class), eq(1L))).thenReturn(competitionDto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteCompetitionSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(competitionService).deleteCompetition(1L);
    }

    @Test
    void shouldReturnNotFoundWhenCompetitionDoesNotExist() throws Exception {
        when(competitionService.getCompetitionById(99L))
                .thenThrow(new EntityNotFoundException("Competition not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }
}
