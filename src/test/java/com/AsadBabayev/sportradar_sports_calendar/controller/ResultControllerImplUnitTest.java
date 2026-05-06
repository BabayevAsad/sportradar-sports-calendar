package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.ResultControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.FootballResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.ResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.exception.GlobalExceptionHandler;
import com.AsadBabayev.sportradar_sports_calendar.service.ResultService;
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
class ResultControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private ResultService resultService;

    @InjectMocks
    private ResultControllerImpl resultController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/results";

    private ResultDTO footballResultResponse;
    private FootballResultRequestDTO footballRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(resultController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        footballResultResponse = new ResultDTO() {
            {
                setId(1L);
                setWinner("liverpool-fc");
                setMessage("Clinical finishing");
                setSportName("FOOTBALL");
                setCompetitionId(1L);
            }
        };

        footballRequest = FootballResultRequestDTO.builder()
                .eventId(100L)
                .winner("liverpool-fc")
                .message("Clinical finishing")
                .homeGoals(3)
                .awayGoals(1)
                .build();
    }

    @Test
    void shouldReturnAllResults() throws Exception {
        when(resultService.getAllResults()).thenReturn(List.of(footballResultResponse));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sportName").value("FOOTBALL"))
                .andExpect(jsonPath("$[0].winner").value("liverpool-fc"));
    }

    @Test
    void shouldReturnResultByEventId() throws Exception {
        when(resultService.getResultByEventId(100L)).thenReturn(footballResultResponse);

        mockMvc.perform(get(BASE_URL + "/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.message").value("Clinical finishing"));
    }

    @Test
    void shouldCreateResultWithPolymorphicBody() throws Exception {
        when(resultService.saveResult(any(ResultRequestDTO.class))).thenReturn(footballResultResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(footballRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateResult() throws Exception {
        when(resultService.updateResult(any(ResultRequestDTO.class), eq(100L))).thenReturn(footballResultResponse);

        mockMvc.perform(put(BASE_URL + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(footballRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteResult() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/100"))
                .andExpect(status().isNoContent());

        verify(resultService).deleteResult(100L);
    }


    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
        footballRequest.setWinner("INVALID WINNER!");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(footballRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenResultDoesNotExist() throws Exception {
        when(resultService.getResultByEventId(99L)).thenThrow(new EntityNotFoundException("Result not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }
}