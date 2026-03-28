package com.AsadBabayev.sportradar_sports_calendar.dto.Venue;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
    private Long id;

    private String name;
    private String city;
    private String country;
}
