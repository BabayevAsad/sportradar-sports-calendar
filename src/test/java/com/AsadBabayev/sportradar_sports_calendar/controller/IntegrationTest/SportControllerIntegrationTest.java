package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.repository.SportRepository;
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
class SportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private ObjectMapper mapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    private static final String BASE_URL = "/rest/api/sports";

    private Long existingSportId;

    @BeforeEach
    void setUp() {
        sportRepository.deleteAll();

        Sport sport = new Sport();
        sport.setName(SportType.FOOTBALL);
        Sport savedSport = sportRepository.save(sport);
        existingSportId = savedSport.getId();
    }

    @Test
    void shouldReturnAllSports() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("FOOTBALL"));
    }

    @Test
    void shouldReturnSportById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingSportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingSportId))
                .andExpect(jsonPath("$.name").value("FOOTBALL"));
    }

    @Test
    void shouldCreateAndPersistSport() throws Exception {
        SportRequestDto requestDto = new SportRequestDto();
        requestDto.setName(SportType.BASKETBALL);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("BASKETBALL"));

        assertThat(sportRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingSport() throws Exception {
        SportRequestDto updateDto = new SportRequestDto();
        updateDto.setName(SportType.TENNIS);

        mockMvc.perform(put(BASE_URL + "/" + existingSportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TENNIS"));
    }

    @Test
    void shouldDeleteSportFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingSportId))
                .andExpect(status().isNoContent());

        assertThat(sportRepository.findById(existingSportId)).isEmpty();
    }

    @Test
    void shouldReturn404WhenSportNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}