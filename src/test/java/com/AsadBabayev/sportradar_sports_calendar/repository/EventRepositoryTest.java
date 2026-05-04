package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.*;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.AsadBabayev.sportradar_sports_calendar.entity.SportType.FOOTBALL;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SportradarSportsCalendarApplication.class)
@ActiveProfiles("test")
class EventRepositoryTest {

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
    }

    @Test
    void findByDate() {
    }

    @Test
    void findByCompetition_Id() {
        var result = eventRepository.findByCompetition_Id(savedCompetition.getId());
        assertFalse(result.isEmpty());
        assertEquals("Champions League", result.get(0).getCompetition().getName());
    }

    @Test
    void findById() {
    }

    @Test
    void findByCompetition_Sport_Name() {
    }
}