package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Competition;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.repository.CompetitionRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SportradarSportsCalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CompetitionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompetitionRepository competitionRepository;

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

    private static final String BASE_URL = "/rest/api/competitions";

    private Long existingCompetitionId;
    private Long sharedSportId;

    @BeforeEach
    void setUp() {
        competitionRepository.deleteAll();
        sportRepository.deleteAll();

        Sport sport = new Sport();
        sport.setName(SportType.FOOTBALL);
        Sport savedSport = sportRepository.save(sport);
        sharedSportId = savedSport.getId();

        Competition competition = new Competition();
        competition.setName("Premier League");
        competition.setSport(savedSport);
        Competition savedCompetition = competitionRepository.save(competition);
        existingCompetitionId = savedCompetition.getId();
    }

    @Test
    void shouldReturnAllCompetitions() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Premier League"));
    }

    @Test
    void shouldReturnCompetitionById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingCompetitionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingCompetitionId))
                .andExpect(jsonPath("$.name").value("Premier League"));
    }

    @Test
    void shouldCreateAndPersistCompetition() throws Exception {
        CompetitionRequestDTO requestDto = new CompetitionRequestDTO();
        requestDto.setName("La Liga");
        requestDto.setSportId(sharedSportId);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("La Liga"));

        assertThat(competitionRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingCompetition() throws Exception {
        CompetitionRequestDTO updateDto = new CompetitionRequestDTO();
        updateDto.setName("Champions League");
        updateDto.setSportId(sharedSportId);

        mockMvc.perform(put(BASE_URL + "/" + existingCompetitionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Champions League"));
    }

    @Test
    void shouldDeleteCompetitionFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingCompetitionId))
                .andExpect(status().isNoContent());

        assertThat(competitionRepository.findById(existingCompetitionId)).isEmpty();
    }

    @Test
    void shouldReturn404WhenCompetitionNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}