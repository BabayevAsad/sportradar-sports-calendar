package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Venue;
import com.AsadBabayev.sportradar_sports_calendar.repository.EventRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.ResultRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.VenueRepository;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SportradarSportsCalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VenueControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ObjectMapper mapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    private static final String BASE_URL = "/rest/api/venues";
    private Long existingVenueId;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        eventRepository.deleteAll();
        venueRepository.deleteAll();

        Venue venue = new Venue();
        venue.setName("Wembley Stadium");
        venue.setCity("London");
        venue.setCountry("United Kingdom");

        venue = venueRepository.save(venue);
        existingVenueId = venue.getId();
    }

    @Test
    void shouldReturnAllVenues() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Wembley Stadium"))
                .andExpect(jsonPath("$[0].city").value("London"));
    }

    @Test
    void shouldReturnVenueById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingVenueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingVenueId))
                .andExpect(jsonPath("$.name").value("Wembley Stadium"));
    }

    @Test
    void shouldCreateAndPersistVenue() throws Exception {
        VenueRequestDTO request = new VenueRequestDTO();
        request.setName("Camp Nou");
        request.setCity("Barcelona");
        request.setCountry("Spain");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Camp Nou"));

        assertThat(venueRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingVenue() throws Exception {
        VenueRequestDTO updateRequest = new VenueRequestDTO();
        updateRequest.setName("Old Trafford");
        updateRequest.setCity("Manchester");
        updateRequest.setCountry("United Kingdom");

        mockMvc.perform(put(BASE_URL + "/" + existingVenueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Old Trafford"));

        Venue updatedVenue = venueRepository.findById(existingVenueId).orElseThrow();
        assertThat(updatedVenue.getName()).isEqualTo("Old Trafford");
        assertThat(updatedVenue.getCity()).isEqualTo("Manchester");
    }

    @Test
    void shouldDeleteVenueFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingVenueId))
                .andExpect(status().isNoContent());

        assertThat(venueRepository.existsById(existingVenueId)).isFalse();
    }

    @Test
    void shouldReturn404WhenVenueNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}