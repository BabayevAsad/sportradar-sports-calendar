package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Competition.CompetitionRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Competition;
import com.AsadBabayev.sportradar_sports_calendar.mapper.CompetitionMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.CompetitionRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.CompetitionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionMapper competitionMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "competitions", key = "'all'")
    @Override
    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.findAll()
                .stream()
                .map(competitionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "competitions", key = "#id")
    @Override
    public CompetitionDTO getCompetitionById(Long id) {
        return competitionMapper.toDTO(findByIdInternal(id));
    }

    @Transactional
    @CachePut(value = "competitions", key = "#result.id")
    @CacheEvict(value = "competitions", key = "'all'")
    @Override
    public CompetitionDTO saveCompetition(CompetitionRequestDTO requestDTO) {
        Competition competition = new Competition();

        competitionMapper.updateEntityFromDto(requestDTO, competition);

        Competition savedCompetition = competitionRepository.save(competition);
        return competitionMapper.toDTO(savedCompetition);
    }

    @Transactional
    @CachePut(value = "competitions", key = "#id")
    @CacheEvict(value = "competitions", key = "'all'")
    @Override
    public CompetitionDTO updateCompetition(CompetitionRequestDTO requestDTO, Long id) {
        Competition competition = findByIdInternal(id);

        competitionMapper.updateEntityFromDto(requestDTO, competition);

        Competition updatedCompetition = competitionRepository.save(competition);
        return competitionMapper.toDTO(updatedCompetition);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "competitions", key = "#id"),
            @CacheEvict(value = "competitions", key = "'all'")
    })
    @Override
    public void deleteCompetition(Long id) {
        Competition competition = findByIdInternal(id);
        competitionRepository.delete(competition);
    }

    private Competition findByIdInternal(Long id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id: " + id));
    }
}