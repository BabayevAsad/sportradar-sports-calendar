package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.FootballResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.*;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.FootballResult;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SportradarSportsCalendarApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ResultControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ObjectMapper mapper;

    private Team sharedHomeTeam;
    private Team sharedAwayTeam;
    private Stage sharedStage;
    private Venue sharedVenue;
    private Competition sharedCompetition;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    private static final String BASE_URL = "/rest/api/results";
    private Long existingEventId;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        eventRepository.deleteAll();
        stageRepository.deleteAll();
        venueRepository.deleteAll();
        teamRepository.deleteAll();
        competitionRepository.deleteAll();
        sportRepository.deleteAll();

        Sport sport = new Sport();
        sport.setName(SportType.FOOTBALL);
        sport = sportRepository.save(sport);

        sharedCompetition = new Competition();
        sharedCompetition.setName("Premier League");
        sharedCompetition.setSport(sport);
        sharedCompetition = competitionRepository.save(sharedCompetition);

        sharedHomeTeam = new Team();
        sharedHomeTeam.setName("Liverpool FC");
        sharedHomeTeam.setSlug("liverpool-fc");
        sharedHomeTeam.setAbbreviation("LIV");
        sharedHomeTeam = teamRepository.save(sharedHomeTeam);

        sharedAwayTeam = new Team();
        sharedAwayTeam.setName("Chelsea FC");
        sharedAwayTeam.setSlug("chelsea-fc");
        sharedAwayTeam.setAbbreviation("CHE");
        sharedAwayTeam = teamRepository.save(sharedAwayTeam);

        sharedStage = new Stage();
        sharedStage.setName("Regular Season");
        sharedStage = stageRepository.save(sharedStage);

        sharedVenue = new Venue();
        sharedVenue.setName("Wembley Stadium");
        sharedVenue.setCity("London");
        sharedVenue.setCountry("United Kingdom");
        sharedVenue = venueRepository.save(sharedVenue);

        Event event = new Event();
        event.setCompetition(sharedCompetition);
        event.setHomeTeam(sharedHomeTeam);
        event.setAwayTeam(sharedAwayTeam);
        event.setDate(LocalDate.now());
        event.setTimeUtc(LocalTime.now());
        event.setStage(sharedStage);
        event.setVenue(sharedVenue);
        event.setStatus("Scheduled");
        event = eventRepository.save(event);
        existingEventId = event.getId();

        FootballResult result = new FootballResult();
        result.setEvent(event);
        result.setWinner("liverpool-fc");
        result.setMessage("Clinical finishing");
        result.setHomeGoals(3);
        result.setAwayGoals(1);
        resultRepository.save(result);
    }

    @Test
    void shouldReturnAllResults() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].winner").value("liverpool-fc"));
    }

    @Test
    void shouldReturnResultByEventId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingEventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("liverpool-fc"))
                .andExpect(jsonPath("$.message").value("Clinical finishing"));
    }

    @Test
    void shouldCreateAndPersistResult() throws Exception {
        Event newEvent = new Event();
        newEvent.setCompetition(sharedCompetition);
        newEvent.setHomeTeam(sharedHomeTeam);
        newEvent.setAwayTeam(sharedAwayTeam);
        newEvent.setDate(LocalDate.now());
        newEvent.setTimeUtc(LocalTime.now());
        newEvent.setStage(sharedStage);
        newEvent.setVenue(sharedVenue);
        newEvent.setStatus("Scheduled");
        newEvent = eventRepository.save(newEvent);

        FootballResultRequestDTO request = FootballResultRequestDTO.builder()
                .eventId(newEvent.getId())
                .winner("man-city")
                .message("Tough match")
                .homeGoals(0)
                .awayGoals(2)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertThat(resultRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingResult() throws Exception {
        FootballResultRequestDTO updateRequest = FootballResultRequestDTO.builder()
                .eventId(existingEventId)
                .winner("draw")
                .message("Late equalizer")
                .homeGoals(2)
                .awayGoals(2)
                .build();

        mockMvc.perform(put(BASE_URL + "/" + existingEventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner").value("draw"));
    }

    @Test
    void shouldDeleteResultFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingEventId))
                .andExpect(status().isNoContent());

        assertThat(resultRepository.findAll()).isEmpty();
    }

    @Test
    void shouldReturn404WhenResultNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}