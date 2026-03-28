package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IceHockeyResultDTO extends ResultDTO {
    private Integer homeGoals;
    private Integer awayGoals;
    private List<String> periodScores;
}