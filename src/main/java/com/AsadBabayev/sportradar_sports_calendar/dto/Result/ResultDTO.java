package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "sportName", "competitionId", "winner", "message" })
public abstract class ResultDTO {
    private Long id;
    private String winner;
    private String message;

    private String sportName;
    private Long competitionId;
}
