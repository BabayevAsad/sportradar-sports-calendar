package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.*;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.AsadBabayev.sportradar_sports_calendar.entity.SportType.FOOTBALL;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SportradarSportsCalendarApplication.class)
@ActiveProfiles("test")
class EventRepositoryIT {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private TeamRepository teamRepository;

    private Event savedEvent;
    private Competition savedCompetition;
    private Sport savedSport;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        Sport sport = Sport.builder()
                .name(FOOTBALL)
                .build();
        savedSport = sportRepository.save(sport);

        Competition competition = Competition.builder()
                .name("Champions League")
                .sport(savedSport)
                .build();
        savedCompetition = competitionRepository.save(competition);

        Stage stage = Stage.builder()
                .name("Final")
                .build();
        stageRepository.save(stage);

        Team home = teamRepository.save(
                Team.builder()
                        .name("Real Madrid")
                        .slug("real-madrid")
                        .abbreviation("RMA")
                        .countryCode("ESP")
                        .build()
        );

        Team away = teamRepository.save(
                Team.builder()
                        .name("AC Milan")
                        .slug("ac-milan")
                        .abbreviation("ACM")
                        .countryCode("ITA")
                        .build()
        );

        Venue stadium = venueRepository.save(
                Venue.builder()
                        .name("Bernabéu")
                        .build()
        );

        Event event = Event.builder()
                .competition(competition)
                .stage(stage)
                .homeTeam(home)
                .awayTeam(away)
                .venue(stadium)
                .date(LocalDate.of(2024, 5, 20))
                .timeUtc(LocalTime.of(20, 45))
                .status("SCHEDULED")
                .build();

        savedEvent = eventRepository.save(event);
    }

    @Test
    void findAll() {
        var pageRequest = PageRequest.of(0, 10);
        var result = eventRepository.findAll(pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Champions League", result.getContent().get(0).getCompetition().getName());
    }

    @Test
    void findByDate() {
        List<Event> result = eventRepository.findByDate(LocalDate.of(2024, 5, 20));

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(savedEvent.getId(), result.get(0).getId());
    }

    @Test
    void findByCompetition_Id() {
        var result = eventRepository.findByCompetition_Id(savedCompetition.getId());

        assertFalse(result.isEmpty());
        assertEquals("Champions League", result.get(0).getCompetition().getName());
        assertEquals(savedCompetition.getId(), result.get(0).getCompetition().getId());
    }

    @Test
    void findById() {
        Optional<Event> result = eventRepository.findById(savedEvent.getId());

        assertTrue(result.isPresent());
        assertEquals("SCHEDULED", result.get().getStatus());
        assertEquals(FOOTBALL, result.get().getCompetition().getSport().getName());
    }

    @Test
    void findByCompetition_Sport_Name() {
        List<Event> result = eventRepository.findByCompetition_Sport_Name(FOOTBALL);

        assertFalse(result.isEmpty());
        assertEquals(FOOTBALL, result.get(0).getCompetition().getSport().getName());
    }
}