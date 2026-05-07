package com.AsadBabayev.sportradar_sports_calendar.dto.Team;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","name","officialName","slug","abbreviation","teamCountryCode","stagePosition"})
public class TeamDTO {
    private Long id;

    private String name;
    private String officialName;
    private String slug;
    private String abbreviation;
    private String teamCountryCode;

    private Integer stagePosition;
}
