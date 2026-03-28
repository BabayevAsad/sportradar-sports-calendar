package com.AsadBabayev.sportradar_sports_calendar.dto.Team;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequestDTO {

    @NotBlank(message = "Team name is required")
    @Size(max = 50, message = "Team name is too long")
    private String name;

    @NotBlank(message = "Official name is required")
    @Size(max = 100, message = "Official name is too long")
    private String officialName;

    @Pattern(regexp = "^[a-z0-9-]+$",
            message = "Slug must contain only lowercase letters, numbers, and hyphens (e.g., 'al-shabab-fc')"
    )
    @NotBlank(message = "Slug is required")
    private String slug;

    @NotBlank(message = "Abbreviation is required")
    @Size(min = 2, max = 5, message = "Abbreviation must be between 2 and 5 characters")
    private String abbreviation;

    @Pattern(regexp = "^[A-Z]{2,3}$", message = "Country code must be 2 or 3 uppercase letters (e.g., ESP, GB)")
    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 3, message = "Country code must be 2 or 3 characters (e.g., ESP)")
    private String teamCountryCode;

    @Min(value = 1, message = "Stage position must be at least 1")
    @Builder.Default
    private Integer stagePosition = 1;
}
