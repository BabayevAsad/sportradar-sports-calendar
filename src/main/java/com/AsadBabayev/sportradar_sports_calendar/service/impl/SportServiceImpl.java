package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.mapper.SportMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.SportRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.SportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    @Transactional(readOnly = true)
    @Override
    public List<SportDto> getAllSports() {
        return sportRepository.findAll()
                .stream()
                .map(sportMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SportDto getSportById(Long id) {
        return sportMapper.toDTO(findByIdInternal(id));
    }

    @Transactional
    @Override
    public SportDto saveSport(SportRequestDto requestDTO) {
        if (sportRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("Sport category already exists: " + requestDTO.getName());
        }

        Sport sport = new Sport();
        sportMapper.updateEntityFromDto(requestDTO, sport);

        return sportMapper.toDTO(sportRepository.save(sport));
    }

    @Transactional
    @Override
    public SportDto updateSport(SportRequestDto requestDTO, Long id) {
        Sport sport = findByIdInternal(id);

        sportMapper.updateEntityFromDto(requestDTO, sport);

        return sportMapper.toDTO(sportRepository.save(sport));
    }

    @Transactional
    @Override
    public void deleteSport(Long id) {
        Sport sport = findByIdInternal(id);
        sportRepository.delete(sport);
    }

    private Sport findByIdInternal(Long id) {
        return sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: " + id));
    }
}