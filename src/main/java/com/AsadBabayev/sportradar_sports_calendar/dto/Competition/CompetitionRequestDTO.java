package com.AsadBabayev.sportradar_sports_calendar.dto.Competition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionRequestDTO {

    @NotBlank(message = "Competition name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Sport ID is required to categorize this competition")
    private Long sportId;
}
