package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Venue;

import org.springframework.stereotype.Component;

@Component
public class VenueMapper {
    public VenueDTO mapToDTO(Venue venue) {
        if (venue == null) return null;

        return VenueDTO.builder()
                .id(venue.getId())
                .name(venue.getName())
                .city(venue.getCity())
                .country(venue.getCountry())
                .build();
    }

    public void updateEntityFromDto(VenueRequestDTO dto, Venue venue) {
        if (dto == null || venue == null) return;

        venue.setName(dto.getName());
        venue.setCity(dto.getCity());
        venue.setCountry(dto.getCountry());
    }
}
