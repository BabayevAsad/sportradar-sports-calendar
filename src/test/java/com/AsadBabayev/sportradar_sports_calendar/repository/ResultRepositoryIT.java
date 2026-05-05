package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.*;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.FootballResult;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.Result;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SportradarSportsCalendarApplication.class)
@ActiveProfiles("test")
class ResultRepositoryIT {

    @Autowired private ResultRepository resultRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private CompetitionRepository competitionRepository;
    @Autowired private SportRepository sportRepository;
    @Autowired private StageRepository stageRepository;

    private Event savedEvent;
    private FootballResult savedResult;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();

        Sport sport = sportRepository.save(
                Sport.builder().name(SportType.FOOTBALL).build()
        );

        Stage stage = stageRepository.save(
                Stage.builder().name("Final").build()
        );

        Competition comp = competitionRepository.save(
                Competition.builder().name("CL").sport(sport).build()
        );

        Team home = teamRepository.save(
                Team.builder()
                        .name("Home")
                        .slug("home")
                        .abbreviation("HOM")
                        .countryCode("US")
                        .build()
        );

        Team away = teamRepository.save(
                Team.builder()
                        .name("Away")
                        .slug("away")
                        .abbreviation("AWY")
                        .countryCode("US")
                        .build()
        );

        savedEvent = eventRepository.save(
                Event.builder()
                        .competition(comp)
                        .stage(stage)
                        .homeTeam(home)
                        .awayTeam(away)
                        .date(LocalDate.now())
                        .timeUtc(LocalTime.now())
                        .status("FINISHED")
                        .build()
        );

        FootballResult footballResult = FootballResult.builder()
                .event(savedEvent)
                .winner("Home")
                .message("Match finished successfully")
                .homeGoals(2)
                .awayGoals(1)
                .goals(List.of("10' Messi", "30' Ronaldo"))
                .build();

        savedResult = resultRepository.saveAndFlush(footballResult);
    }

    @Test
    void findByEventId_ShouldReturnFootballResultWithDetails() {
        Optional<Result> found = resultRepository.findByEventId(savedEvent.getId());

        assertTrue(found.isPresent(), "Result should be found");

        if (found.get() instanceof FootballResult fr) {
            assertEquals(2, fr.getHomeGoals());
            assertEquals(1, fr.getAwayGoals());
            assertEquals(2, fr.getGoals().size());
            assertEquals("Home", fr.getWinner());
        } else {
            fail("Result should be a FootballResult instance");
        }
    }

    @Test
    void findByEventId_ShouldReturnEmpty_WhenNotFound() {
        Optional<Result> found = resultRepository.findByEventId(9999L);
        assertTrue(found.isEmpty(), "Result should be empty");
    }
}