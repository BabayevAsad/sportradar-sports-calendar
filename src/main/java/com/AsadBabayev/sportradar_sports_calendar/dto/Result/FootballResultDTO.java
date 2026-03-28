package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FootballResultDTO extends ResultDTO {
    private Integer homeGoals;
    private Integer awayGoals;
    private List<String> goals;
    private List<String> yellowCards;
    private List<String> secondYellowCards;
    private List<String> directRedCards;
}