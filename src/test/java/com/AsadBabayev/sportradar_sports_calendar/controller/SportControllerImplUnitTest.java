package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.SportControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.service.SportService;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SportControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private SportService sportService;

    @InjectMocks
    private SportControllerImpl sportController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/sports";

    private SportDto sportDto;
    private SportRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sportController).build();

        sportDto = new SportDto();
        sportDto.setId(1L);
        sportDto.setName(SportType.FOOTBALL.toString());

        requestDto = new SportRequestDto();
        requestDto.setName(SportType.FOOTBALL);
    }

    @Test
    void shouldReturnAllSports() throws Exception {
        when(sportService.getAllSports()).thenReturn(List.of(sportDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("FOOTBALL"));
    }

    @Test
    void shouldReturnSportById() throws Exception {
        when(sportService.getSportById(1L)).thenReturn(sportDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("FOOTBALL"));
    }

    @Test
    void shouldCreateSportAndReturn201() throws Exception {
        when(sportService.saveSport(any(SportRequestDto.class))).thenReturn(sportDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("FOOTBALL"));
    }

    @Test
    void shouldUpdateSportSuccessfully() throws Exception {
        when(sportService.updateSport(any(SportRequestDto.class), eq(1L))).thenReturn(sportDto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteSportSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(sportService).deleteSport(1L);
    }

    @Test
    void shouldReturnNotFoundWhenSportDoesNotExist() throws Exception {
        when(sportService.getSportById(99L))
                .thenThrow(new EntityNotFoundException("Sport not found"));

        assertThatThrownBy(() -> mockMvc.perform(get(BASE_URL + "/99")))
                .hasCauseInstanceOf(EntityNotFoundException.class);
    }
}
