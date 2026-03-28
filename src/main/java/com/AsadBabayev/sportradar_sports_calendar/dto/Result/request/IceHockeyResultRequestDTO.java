package com.AsadBabayev.sportradar_sports_calendar.dto.Result.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IceHockeyResultRequestDTO extends ResultRequestDTO {

    @Min(value = 0, message = "Home goals cannot be negative")
    private Integer homeGoals;

    @Min(value = 0, message = "Away goals cannot be negative")
    private Integer awayGoals;

    @Builder.Default
    private List<String> periodScores = new ArrayList<>();
}