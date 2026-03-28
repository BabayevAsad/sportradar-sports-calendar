package com.AsadBabayev.sportradar_sports_calendar.dto.Result.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FootballResultRequestDTO extends ResultRequestDTO {
    @NotNull(message = "Home goals are required")
    @Min(value = 0, message = "Home goals cannot be negative")
    private Integer homeGoals;

    @NotNull(message = "Away goals are required")
    @Min(value = 0, message = "Away goals cannot be negative")
    private Integer awayGoals;

    @Builder.Default
    private List<String> goals = new ArrayList<>();

    @Builder.Default
    private List<String> yellowCards = new ArrayList<>();

    @Builder.Default
    private List<String> secondYellowCards = new ArrayList<>();

    @Builder.Default
    private List<String> directRedCards = new ArrayList<>();
}

