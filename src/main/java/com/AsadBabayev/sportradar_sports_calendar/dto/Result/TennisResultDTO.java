package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "sportName", "competitionId", "winner", "message","homeSets","awaySets","setScores"})
public class TennisResultDTO extends ResultDTO {
    private Integer homeSets;
    private Integer awaySets;
    private List<String> setScores;
}