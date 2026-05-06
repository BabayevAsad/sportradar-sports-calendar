package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Team;
import com.AsadBabayev.sportradar_sports_calendar.repository.EventRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.ResultRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.TeamRepository;
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
class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

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

    private static final String BASE_URL = "/rest/api/teams";
    private Long existingTeamId;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        eventRepository.deleteAll();
        teamRepository.deleteAll();

        Team team = new Team();
        team.setName("Real Madrid");
        team.setOfficialName("Real Madrid Club de Fútbol");
        team.setSlug("real-madrid");
        team.setAbbreviation("RMA");
        team.setCountryCode("ESP");
        team.setStagePosition(1);

        team = teamRepository.save(team);
        existingTeamId = team.getId();
    }

    @Test
    void shouldReturnAllTeams() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Real Madrid"));
    }

    @Test
    void shouldReturnTeamById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingTeamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingTeamId))
                .andExpect(jsonPath("$.slug").value("real-madrid"));
    }

    @Test
    void shouldCreateAndPersistTeam() throws Exception {
        TeamRequestDTO request = TeamRequestDTO.builder()
                .name("FC Barcelona")
                .officialName("Futbol Club Barcelona")
                .slug("fc-barcelona")
                .abbreviation("BAR")
                .teamCountryCode("ESP")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("FC Barcelona"));

        assertThat(teamRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingTeam() throws Exception {
        TeamRequestDTO updateRequest = TeamRequestDTO.builder()
                .name("Real Madrid Updated")
                .officialName("Real Madrid CF")
                .slug("real-madrid-updated")
                .abbreviation("RMA")
                .teamCountryCode("ESP")
                .build();

        mockMvc.perform(put(BASE_URL + "/" + existingTeamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Real Madrid Updated"));

        Team updatedTeam = teamRepository.findById(existingTeamId).orElseThrow();
        assertThat(updatedTeam.getSlug()).isEqualTo("real-madrid-updated");
    }

    @Test
    void shouldDeleteTeamFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingTeamId))
                .andExpect(status().isNoContent());

        assertThat(teamRepository.existsById(existingTeamId)).isFalse();
    }

    @Test
    void shouldReturn400WhenMandatoryFieldsMissing() throws Exception {
        TeamRequestDTO invalidRequest = TeamRequestDTO.builder()
                .name("Invalid Team")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenTeamNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}