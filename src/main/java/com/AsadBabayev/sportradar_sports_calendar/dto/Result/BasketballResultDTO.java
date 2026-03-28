package com.AsadBabayev.sportradar_sports_calendar.dto.Result;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BasketballResultDTO extends ResultDTO {
    private Integer homeTotalPoints;
    private Integer awayTotalPoints;
    private List<Integer> quarterPoints;
}