package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.starter.SportradarSportsCalendarApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = SportradarSportsCalendarApplication.class)
@ActiveProfiles("test")
class SportRepositoryIT {

    @Autowired
    private SportRepository sportRepository;

    @Test
    void existsByName_ShouldReturnTrue_WhenSportExists() {
        sportRepository.save(
                Sport.builder()
                        .name(SportType.FOOTBALL)
                        .build()
        );

        boolean exists = sportRepository.existsByName(SportType.FOOTBALL);

        assertTrue(exists);
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenSportDoesNotExist() {
        boolean exists = sportRepository.existsByName(SportType.BASKETBALL);

        assertFalse(exists);
    }
}
