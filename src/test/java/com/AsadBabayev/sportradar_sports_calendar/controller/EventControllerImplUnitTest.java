package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.EventControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventResponseDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventControllerImpl eventController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/events";

    private EventDTO footballEventDto;
    private EventRequestDTO requestDto;
    private EventResponseDTO responsePageDto;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        footballEventDto = EventDTO.builder()
                .id(1L)
                .sportName("FOOTBALL")
                .originCompetitionName("Premier League")
                .dateVenue(LocalDate.now().plusDays(1))
                .timeVenueUTC(LocalTime.of(20, 0))
                .status("scheduled")
                .build();

        requestDto = EventRequestDTO.builder()
                .competitionId("sr:comp:1")
                .stageId("sr:stage:1")
                .date(LocalDate.now().plusDays(1))
                .timeUtc(LocalTime.of(20, 0))
                .status("scheduled")
                .venueId(1L)
                .homeTeamSlug("team-a")
                .awayTeamSlug("team-b")
                .build();

        responsePageDto = EventResponseDTO.builder()
                .data(List.of(footballEventDto))
                .currentPage(0)
                .totalElements(1L)
                .isLast(true)
                .build();
    }

    @Test
    void shouldReturnEventDtoForGivenId() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(footballEventDto);
        mockMvc.perform(get(BASE_URL + "/1")).andExpect(status().isOk());
    }

    @Test
    void shouldReturnPaginatedListOfEvents() throws Exception {
        when(eventService.getAllEvents(any(Pageable.class))).thenReturn(responsePageDto);
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());
    }

    @Test
    void shouldSaveEventAndReturnDto() throws Exception {
        when(eventService.saveEvent(any(EventRequestDTO.class))).thenReturn(footballEventDto);
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateEventAndReturnDto() throws Exception {
        when(eventService.updateEvent(any(EventRequestDTO.class), eq(1L))).thenReturn(footballEventDto);
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")).andExpect(status().isNoContent());
        verify(eventService).deleteEvent(1L);
    }

    @Test
    void shouldReturnEventsBySportType() throws Exception {
        when(eventService.getEventsBySport(SportType.FOOTBALL)).thenReturn(List.of(footballEventDto));
        mockMvc.perform(get(BASE_URL + "/by-sport/FOOTBALL")).andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsInvalid() throws Exception {
        requestDto.setStatus("wrong");
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDateIsInPast() throws Exception {
        requestDto.setDate(LocalDate.now().minusDays(1));
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowExceptionWhenEventDoesNotExist() {
        when(eventService.getEventById(99L)).thenThrow(new EntityNotFoundException("Not found"));

        assertThrows(Exception.class, () -> {
            mockMvc.perform(get(BASE_URL + "/99"));
        });
    }
}