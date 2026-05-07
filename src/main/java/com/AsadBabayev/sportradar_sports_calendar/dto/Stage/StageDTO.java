package com.AsadBabayev.sportradar_sports_calendar.dto.Stage;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","name","ordering"})
public class StageDTO {
    private Long id;
    private String name;

    private Integer ordering;
}
