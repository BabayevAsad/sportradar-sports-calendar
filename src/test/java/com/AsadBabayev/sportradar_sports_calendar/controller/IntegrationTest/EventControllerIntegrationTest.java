package com.AsadBabayev.sportradar_sports_calendar.controller.IntegrationTest;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.*;
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
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    private Competition sharedCompetition;
    private Stage sharedStage;
    private Venue sharedVenue;
    private Team sharedHomeTeam;
    private Team sharedAwayTeam;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    private static final String BASE_URL = "/rest/api/events";
    private Long existingEventId;

    @BeforeEach
    void setUp() {
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
        sharedVenue.setName("Anfield");
        sharedVenue.setCity("Liverpool");
        sharedVenue.setCountry("UK");
        sharedVenue = venueRepository.save(sharedVenue);

        Event event = new Event();
        event.setCompetition(sharedCompetition);
        event.setHomeTeam(sharedHomeTeam);
        event.setAwayTeam(sharedAwayTeam);
        event.setDate(LocalDate.now().plusDays(1));
        event.setTimeUtc(LocalTime.of(20, 0));
        event.setStage(sharedStage);
        event.setVenue(sharedVenue);
        event.setStatus("Scheduled");
        event = eventRepository.save(event);
        existingEventId = event.getId();
    }

    @Test
    void shouldReturnAllEventsPaginated() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldReturnEventById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + existingEventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingEventId))
                .andExpect(jsonPath("$.originCompetitionName").value("Premier League"));
    }

    @Test
    void shouldCreateAndPersistEvent() throws Exception {
        EventRequestDTO request = EventRequestDTO.builder()
                .competitionId(sharedCompetition.getId())
                .stageId(sharedStage.getId())
                .venueId(sharedVenue.getId())
                .homeTeamSlug(sharedHomeTeam.getSlug())
                .awayTeamSlug(sharedAwayTeam.getSlug())
                .date(LocalDate.now().plusDays(5))
                .timeUtc(LocalTime.of(15, 0))
                .status("scheduled")
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertThat(eventRepository.findAll()).hasSize(2);
    }

    @Test
    void shouldUpdateExistingEvent() throws Exception {
        EventRequestDTO updateRequest = EventRequestDTO.builder()
                .competitionId(sharedCompetition.getId())
                .stageId(sharedStage.getId())
                .venueId(sharedVenue.getId())
                .homeTeamSlug(sharedHomeTeam.getSlug())
                .awayTeamSlug(sharedAwayTeam.getSlug())
                .date(LocalDate.now().plusDays(2))
                .timeUtc(LocalTime.of(21, 0))
                .status("postponed")
                .build();

        mockMvc.perform(put(BASE_URL + "/" + existingEventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("postponed"));
    }

    @Test
    void shouldDeleteEventFromDatabase() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + existingEventId))
                .andExpect(status().isNoContent());

        assertThat(eventRepository.existsById(existingEventId)).isFalse();
    }

    @Test
    void shouldReturnEventsBySportType() throws Exception {
        mockMvc.perform(get(BASE_URL + "/by-sport/FOOTBALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sportName").value("FOOTBALL"));
    }

    @Test
    void shouldReturnEventsByCompetitionId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/by-competition/" + sharedCompetition.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].originCompetitionName").value("Premier League"));
    }

    @Test
    void shouldReturnEventsByDate() throws Exception {
        String dateStr = LocalDate.now().plusDays(1).toString();
        mockMvc.perform(get(BASE_URL + "/by-date/" + dateStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturn404WhenEventNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9999"))
                .andExpect(status().isNotFound());
    }
}