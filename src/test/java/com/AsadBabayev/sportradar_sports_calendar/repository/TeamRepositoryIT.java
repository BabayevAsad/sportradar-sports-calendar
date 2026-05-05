package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.Team;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SportradarSportsCalendarApplication.class)
@ActiveProfiles("test")
class TeamRepositoryIT {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void findBySlug_ShouldReturnTeam_WhenTeamExists() {
        Team team = teamRepository.save(
                Team.builder()
                        .name("Home Team")
                        .slug("home")
                        .abbreviation("HOM")
                        .countryCode("US")
                        .build()
        );

        Optional<Team> result = teamRepository.findBySlug("home");

        assertTrue(result.isPresent());
        assertEquals("home", result.get().getSlug());
    }

    @Test
    void findBySlug_ShouldReturnEmpty_WhenTeamDoesNotExist() {
        Optional<Team> result = teamRepository.findBySlug("unknown");

        assertTrue(result.isEmpty());
    }
}