package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Stage;
import com.AsadBabayev.sportradar_sports_calendar.repository.*;
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
class StageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper mapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    private static final String BASE_URL = "/rest/api/stages";
    private Long existingStageId;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        stageRepository.deleteAll();

        Stage stage = new Stage();
        stage.setName("Regular Season");
        stage.setOrdering(1);
        stage = stageRepository.save(stage);
        existingStageId = stage.getId();
    }

    @Test
    void shouldReturnAllStages() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Regular Season"));
    }

    @Test
    void shouldReturnStageById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingStageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingStageId))
                .andExpect(jsonPath("$.name").value("Regular Season"));
    }

    @Test
    void shouldCreateAndPersistStage() throws Exception {
        StageRequestDTO request = new StageRequestDTO();
        request.setName("Playoffs");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Playoffs"));

        assertThat(stageRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingStage() throws Exception {
        StageRequestDTO updateRequest = new StageRequestDTO();
        updateRequest.setName("Finals");

        mockMvc.perform(put(BASE_URL + "/" + existingStageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Finals"));

        Stage updatedStage = stageRepository.findById(existingStageId).orElseThrow();
        assertThat(updatedStage.getName()).isEqualTo("Finals");
    }

    @Test
    void shouldDeleteStageFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingStageId))
                .andExpect(status().isNoContent());

        assertThat(stageRepository.existsById(existingStageId)).isFalse();
    }

    @Test
    void shouldReturn404WhenStageNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}
