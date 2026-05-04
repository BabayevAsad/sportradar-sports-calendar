package com.AsadBabayev.sportradar_sports_calendar.dto.Event;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    @NotNull(message = "Competition ID is required")
    private Long competitionId;

    @NotNull(message = "Stage ID is required")
    private Long stageId;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Match date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime timeUtc;

    @Pattern(regexp = "^(scheduled|played|cancelled|postponed|live)$",
            message = "Status must be: scheduled, played, cancelled, postponed, or live")
    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Venue ID is required")
    private Long venueId;

    @NotBlank(message = "Home team slug is required")
    private String homeTeamSlug;

    @NotBlank(message = "Away team slug is required")
    private String awayTeamSlug;
}
