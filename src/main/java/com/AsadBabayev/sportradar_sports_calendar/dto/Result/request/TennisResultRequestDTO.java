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
public class TennisResultRequestDTO extends ResultRequestDTO {
    @NotNull(message = "Home sets count is required")
    @Min(value = 0, message = "Sets cannot be negative")
    private Integer homeSets;

    @NotNull(message = "Away sets count is required")
    @Min(value = 0, message = "Sets cannot be negative")
    private Integer awaySets;

    @Builder.Default
    private List<String> setScores = new ArrayList<>();
}
