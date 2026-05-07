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
@JsonPropertyOrder({"id", "sportName", "competitionId", "winner", "message","homeTotalPoints","awayTotalPoints","quarterPoints"})
public class BasketballResultDTO extends ResultDTO {
    private Integer homeTotalPoints;
    private Integer awayTotalPoints;
    private List<Integer> quarterPoints;
}