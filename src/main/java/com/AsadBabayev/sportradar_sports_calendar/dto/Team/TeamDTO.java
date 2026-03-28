package com.AsadBabayev.sportradar_sports_calendar.dto.Team;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Long id;

    private String name;
    private String officialName;
    private String slug;
    private String abbreviation;
    private String teamCountryCode;

    private Integer stagePosition;
}
