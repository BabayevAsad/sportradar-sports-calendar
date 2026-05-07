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
@JsonPropertyOrder({"id", "sportName", "competitionId", "winner", "message","homeGoals","awayGoals","goals","yellowCards","secondYellowCards","directRedCards"})
public class FootballResultDTO extends ResultDTO {
    private Integer homeGoals;
    private Integer awayGoals;
    private List<String> goals;
    private List<String> yellowCards;
    private List<String> secondYellowCards;
    private List<String> directRedCards;
}