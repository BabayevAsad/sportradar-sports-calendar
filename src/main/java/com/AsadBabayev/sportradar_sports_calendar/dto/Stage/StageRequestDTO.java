package com.AsadBabayev.sportradar_sports_calendar.dto.Stage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageRequestDTO {

    @NotBlank(message = "Stage name is required")
    @Size(max = 50, message = "Stage name is too long")
    private String name;

    @NotNull(message = "Ordering is required")
    @Min(value = 1, message = "Ordering must be at least 1")
    @Builder.Default
    private Integer ordering = 1;
}
