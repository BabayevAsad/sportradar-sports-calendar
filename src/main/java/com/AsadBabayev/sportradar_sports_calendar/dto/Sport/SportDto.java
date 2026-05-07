package com.AsadBabayev.sportradar_sports_calendar.dto.Sport;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","name"})
public class SportDto {
    private Long id;
    private String name;
}