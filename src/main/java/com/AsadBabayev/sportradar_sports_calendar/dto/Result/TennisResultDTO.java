package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TennisResultDTO extends ResultDTO {
    private Integer homeSets;
    private Integer awaySets;
    private List<String> setScores;
}