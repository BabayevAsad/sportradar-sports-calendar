package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Competition;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.repository.SportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompetitionMapper {

    private final SportRepository sportRepository;

    public CompetitionDTO toDTO(Competition competition) {
        if (competition == null) return null;

        return CompetitionDTO.builder()
                .id(competition.getId())
                .name(competition.getName())
                .sportName(competition.getSport() != null ? competition.getSport().getName().name() : null)
                .createdTime(competition.getCreatedTime())
                .updatedTime(competition.getUpdatedTime())
                .build();
    }

    public void updateEntityFromDto(CompetitionRequestDTO dto, Competition competition) {
        if (dto == null || competition == null) return;

        if (dto.getId() != null) {
            competition.setId(dto.getId());
        }
        competition.setName(dto.getName());

        if (dto.getSportId() != null) {
            Sport sport = sportRepository.findById(dto.getSportId())
                    .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + dto.getSportId()));
            competition.setSport(sport);
        }
    }
}