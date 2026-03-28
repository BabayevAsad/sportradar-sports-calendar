package com.AsadBabayev.sportradar_sports_calendar.dto.Stage;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDTO {

    private String id;
    private String name;

    private Integer ordering;
}
