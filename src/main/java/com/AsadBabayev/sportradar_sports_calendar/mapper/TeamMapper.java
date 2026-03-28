package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Team;

import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public   TeamDTO toDTO(Team team) {
        if (team == null) return null;

        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .officialName(team.getOfficialName())
                .slug(team.getSlug())
                .abbreviation(team.getAbbreviation())
                .teamCountryCode(team.getCountryCode())
                .stagePosition(team.getStagePosition())
                .build();
    }

    public void updateEntityFromDto(TeamRequestDTO dto, Team team) {
        if (dto == null || team == null) return;

        team.setName(dto.getName());
        team.setOfficialName(dto.getOfficialName());
        team.setSlug(dto.getSlug());
        team.setAbbreviation(dto.getAbbreviation());
        team.setCountryCode(dto.getTeamCountryCode());
        team.setStagePosition(dto.getStagePosition());
    }
}
