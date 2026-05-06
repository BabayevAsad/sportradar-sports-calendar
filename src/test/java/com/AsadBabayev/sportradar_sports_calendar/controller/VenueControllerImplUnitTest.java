package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.controller.impl.VenueControllerImpl;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.exception.GlobalExceptionHandler;
import com.AsadBabayev.sportradar_sports_calendar.service.VenueService;
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
class VenueControllerImplUnitTest {

    private MockMvc mockMvc;

    @Mock
    private VenueService venueService;

    @InjectMocks
    private VenueControllerImpl venueController;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "/rest/api/venues";

    private VenueDTO venueDto;
    private VenueRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(venueController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        venueDto = new VenueDTO();
        venueDto.setId(1L);
        venueDto.setName("Wembley Stadium");
        venueDto.setCity("London");
        venueDto.setCountry("United Kingdom");

        requestDto = new VenueRequestDTO();
        requestDto.setName("Wembley Stadium");
        requestDto.setCity("London");
        requestDto.setCountry("United Kingdom");

    }

    @Test
    void shouldReturnAllVenues() throws Exception {
        when(venueService.getAllVenues()).thenReturn(List.of(venueDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Wembley Stadium"))
                .andExpect(jsonPath("$[0].city").value("London"));
    }

    @Test
    void shouldReturnVenueById() throws Exception {
        when(venueService.getVenueById(1L)).thenReturn(venueDto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Wembley Stadium"));
    }

    @Test
    void shouldCreateVenueAndReturn201() throws Exception {
        when(venueService.saveVenue(any(VenueRequestDTO.class))).thenReturn(venueDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Wembley Stadium"));
    }

    @Test
    void shouldUpdateVenueSuccessfully() throws Exception {
        when(venueService.updateVenue(any(VenueRequestDTO.class), eq(1L))).thenReturn(venueDto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteVenueSuccessfully() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(venueService).deleteVenue(1L);
    }

    @Test
    void shouldReturnNotFoundWhenVenueDoesNotExist() throws Exception {
        when(venueService.getVenueById(99L))
                .thenThrow(new EntityNotFoundException("Venue not found"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }
}