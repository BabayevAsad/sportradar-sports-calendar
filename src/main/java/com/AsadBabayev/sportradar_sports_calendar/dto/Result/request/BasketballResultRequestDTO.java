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
public class BasketballResultRequestDTO extends ResultRequestDTO {
    @NotNull(message = "Home total points are required")
    @Min(value = 0, message = "Home points cannot be negative")
    private Integer homeTotalPoints;

    @NotNull(message = "Away total points are required")
    @Min(value = 0, message = "Away points cannot be negative")
    private Integer awayTotalPoints;

    @Builder.Default
    private List<Integer> quarterPoints = new ArrayList<>();
}
