package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import org.springframework.stereotype.Component;

@Component
public class SportMapper {
    public SportDto toDTO(Sport sport) {
        if (sport == null) {
            return null;
        }

        return SportDto.builder()
                .id(sport.getId())
                .name(sport.getName() != null ? sport.getName().name() : null)
                .build();
    }

    public void updateEntityFromDto(SportRequestDto dto, Sport sport) {
        if (dto == null || sport == null) {
            return;
        }

        sport.setName(dto.getName());
    }
}