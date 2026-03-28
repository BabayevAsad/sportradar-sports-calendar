package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Stage.StageRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Stage;
import com.AsadBabayev.sportradar_sports_calendar.mapper.StageMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.StageRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.StageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;
    private final StageMapper stageMapper;

    @Transactional(readOnly = true)
    @Override
    public List<StageDTO> getAllStages() {
        return stageRepository.findAll()
                .stream()
                .map(stageMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public StageDTO getStageById(String id) {
        return stageMapper.toDTO(findByIdInternal(id));
    }

    @Transactional
    @Override
    public StageDTO saveStage(StageRequestDTO requestDTO) {
        Stage stage = new Stage();

        stageMapper.updateEntityFromDto(requestDTO, stage);

        return stageMapper.toDTO(stageRepository.save(stage));
    }

    @Transactional
    @Override
    public StageDTO updateStage(StageRequestDTO requestDTO, String id) {
        Stage stage = findByIdInternal(id);

        stageMapper.updateEntityFromDto(requestDTO, stage);

        return stageMapper.toDTO(stageRepository.save(stage));
    }

    @Transactional
    @Override
    public void deleteStage(String id) {
        Stage stage = findByIdInternal(id);
        stageRepository.delete(stage);
    }

    private Stage findByIdInternal(String id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stage not found with id: " + id));
    }
}
