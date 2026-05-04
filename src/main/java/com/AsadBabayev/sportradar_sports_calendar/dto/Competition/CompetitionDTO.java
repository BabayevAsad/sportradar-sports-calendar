package com.AsadBabayev.sportradar_sports_calendar.dto.Competition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "sportName", "name", "createdTime", "updatedTime" })
public class CompetitionDTO {
    private Long id;
    private String name;
    private String sportName;

    @JsonIgnore
    private LocalDateTime createdTime;

    @JsonIgnore
    private LocalDateTime updatedTime;
}