package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final TeamMapper teamMapper;
    private final ResultMapper resultMapper;
    private final StageMapper stageMapper;

    public EventDTO mapToDTO(Event event) {
        if (event == null) return null;

        return EventDTO.builder()
                .id(event.getId())
                .sportName(event.getCompetition() != null && event.getCompetition().getSport() != null
                ? event.getCompetition().getSport().getName().name()
                : null)
                .originCompetitionName(event.getCompetition().getName())
                .season(event.getDate() != null ? event.getDate().getYear() : null)
                .status(event.getStatus())
                .dateVenue(event.getDate())
                .timeVenueUTC(event.getTimeUtc())
                .stadium(event.getVenue() != null ? event.getVenue().getName() : "Venue TBD")
                .homeTeam(teamMapper.toDTO(event.getHomeTeam()))
                .awayTeam(teamMapper.toDTO(event.getAwayTeam()))
                .result(event.getResult() != null ? resultMapper.toDTO(event.getResult()) : null)
                .stage(stageMapper.toDTO(event.getStage()))
                .originCompetitionId(event.getCompetition() != null ? event.getCompetition().getId() : null)
                .originCompetitionName(event.getCompetition() != null ? event.getCompetition().getName() : null)
                .build();
    }

    public void updateEntityFromDto(EventRequestDTO dto, Event event) {
        if (dto == null || event == null) return;

        event.setDate(dto.getDate());
        event.setTimeUtc(dto.getTimeUtc());
        event.setStatus(dto.getStatus());
    }
}