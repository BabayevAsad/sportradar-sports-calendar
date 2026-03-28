package com.AsadBabayev.sportradar_sports_calendar.dto.Result.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "sportType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FootballResultRequestDTO.class, name = "FOOTBALL"),
        @JsonSubTypes.Type(value = TennisResultRequestDTO.class, name = "TENNIS"),
        @JsonSubTypes.Type(value = BasketballResultRequestDTO.class, name = "BASKETBALL"),
        @JsonSubTypes.Type(value = IceHockeyResultRequestDTO.class, name = "ICE_HOCKEY")
})
public abstract class ResultRequestDTO {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Winner slug must be lowercase and URL-friendly (e.g., 'real-madrid')"
    )
    @NotBlank(message = "Winner slug is required")
    private String winner;

    @Size(max = 255, message = "Message summary is too long")
    private String message;
}