package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.TeamControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.TeamService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamControllerImpl teamController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/teams";

    private TeamDTO teamDto;
    private TeamRequestDTO teamRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();

        teamDto = TeamDTO.builder()
                .id(1L)
                .name("Real Madrid")
                .officialName("Real Madrid Club de Fútbol")
                .slug("real-madrid")
                .abbreviation("RMA")
                .teamCountryCode("ESP")
                .stagePosition(1)
                .build();

        teamRequest = TeamRequestDTO.builder()
                .name("Real Madrid")
                .officialName("Real Madrid Club de Fútbol")
                .slug("real-madrid")
                .abbreviation("RMA")
                .teamCountryCode("ESP")
                .build();
    }

    @Test
    void shouldReturnAllTeams() throws Exception {
        when(teamService.getAllTeams()).thenReturn(List.of(teamDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Real Madrid"))
                .andExpect(jsonPath("$[0].abbreviation").value("RMA"));
    }

    @Test
    void shouldReturnTeamById() throws Exception {
        when(teamService.getTeamById(1L)).thenReturn(teamDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.officialName").value("Real Madrid Club de Fútbol"));
    }

    @Test
    void shouldCreateTeam() throws Exception {
        when(teamService.saveTeam(any(TeamRequestDTO.class))).thenReturn(teamDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(teamRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teamCountryCode").value("ESP"));
    }

    @Test
    void shouldUpdateTeam() throws Exception {
        when(teamService.updateTeam(any(TeamRequestDTO.class), eq(1L))).thenReturn(teamDto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("real-madrid"));
    }

    @Test
    void shouldDeleteTeam() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(teamService).deleteTeam(1L);
    }


    @Test
    void shouldReturnBadRequestWhenMandatoryFieldsAreMissing() throws Exception {
        TeamRequestDTO invalidRequest = TeamRequestDTO.builder()
                .name("Faulty Team")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenTeamNotFound() {
        when(teamService.getTeamById(99L)).thenThrow(new EntityNotFoundException("Team not found"));

        assertThrows(Exception.class, () -> {
            mockMvc.perform(get(BASE_URL + "/99"));
        });
    }
}