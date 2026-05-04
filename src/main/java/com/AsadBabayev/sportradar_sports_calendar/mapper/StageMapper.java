package com.AsadBabayev.sportradar_sports_calendar.mapper;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Stage;

import org.springframework.stereotype.Component;

@Component
public class StageMapper {
    public StageDTO toDTO(Stage stage) {
        if (stage == null) return null;

        return StageDTO.builder()
                .id(stage.getId())
                .name(stage.getName())
                .ordering(stage.getOrdering())
                .build();
    }

    public void updateEntityFromDto(StageRequestDTO dto, Stage stage) {
        if (dto == null || stage == null) return;

        stage.setName(dto.getName());
        stage.setOrdering(dto.getOrdering());
    }
}
