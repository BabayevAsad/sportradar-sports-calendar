package com.AsadBabayev.sportradar_sports_calendar.dto.Event;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "sportName", "originCompetitionId", "originCompetitionName", "season", "status", "stage",
        "group", "homeTeam", "awayTeam", "dateVenue", "timeVenueUTC", "stadium", "result"})
public class EventDTO {

    private Long id;

    private String sportName;

    private Integer season;

    private String status;

    private LocalDate dateVenue;
    private LocalTime timeVenueUTC;

    private String stadium;

    private TeamDTO homeTeam;
    private TeamDTO awayTeam;

    private ResultDTO result;

    private StageDTO stage;

    private String group;

    private String originCompetitionId;
    private String originCompetitionName;
}
