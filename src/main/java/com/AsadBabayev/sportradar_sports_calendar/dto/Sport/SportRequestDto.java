package com.AsadBabayev.sportradar_sports_calendar.dto.Sport;

import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportRequestDto {

    @NotNull(message = "Sport type is required (FOOTBALL, ICE_HOCKEY, TENNIS, or BASKETBALL)")
    private SportType name;
}