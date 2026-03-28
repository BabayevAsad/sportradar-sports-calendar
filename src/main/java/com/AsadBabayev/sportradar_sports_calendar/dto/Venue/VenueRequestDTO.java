package com.AsadBabayev.sportradar_sports_calendar.dto.Venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequestDTO {
    @NotBlank(message = "Venue name is required")
    @Size(max = 50, message = "Venue name is too long")
    private String name;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City name is too long")
    private String city;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country name is too long")
    private String country;
}
