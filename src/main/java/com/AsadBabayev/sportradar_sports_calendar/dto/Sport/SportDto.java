package com.AsadBabayev.sportradar_sports_calendar.dto.Sport;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportDto {
    private Long id;
    private String name;
}