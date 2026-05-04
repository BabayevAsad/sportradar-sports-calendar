package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventResponseDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.mapper.EventMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.*;
import com.AsadBabayev.sportradar_sports_calendar.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final StageRepository stageRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;


    @Transactional(readOnly = true)
    @Override
    public EventResponseDTO getAllEvents(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAll(pageable);

        return eventMapper.mapToResponseDTO(eventPage);
    }

    @Transactional(readOnly = true)
    @Override
    public EventDTO getEventById(Long id) {
        return eventMapper.mapToDTO(findByIdInternal(id));
    }

    @Transactional
    @Override
    public EventDTO saveEvent(EventRequestDTO dto) {

        validateTeams(dto);

        Event event = new Event();

        mapDtoToEvent(dto, event);

        return eventMapper.mapToDTO(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventDTO updateEvent(EventRequestDTO dto, Long id) {

        validateTeams(dto);

        Event event = findByIdInternal(id);

        mapDtoToEvent(dto, event);

        return eventMapper.mapToDTO(eventRepository.save(event));
    }

    @Transactional
    @Override
    public void deleteEvent(Long id) {
        eventRepository.delete(findByIdInternal(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDTO> getEventByDate(LocalDate date) {
        return eventRepository.findByDate(date)
                .stream()
                .map(eventMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDTO> getEventsBySport(SportType sport) {
        return eventRepository.findByCompetition_Sport_Name(sport)
                .stream()
                .map(eventMapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDTO> getEventByCompetitionId(Long competitionId) {
        return eventRepository.findByCompetition_Id(competitionId)
                .stream()
                .map(eventMapper::mapToDTO)
                .toList();
    }

    private void mapDtoToEvent(EventRequestDTO dto, Event event) {
        eventMapper.updateEntityFromDto(dto, event);

        event.setCompetition(competitionRepository.getReferenceById(dto.getCompetitionId()));
        event.setStage(stageRepository.getReferenceById(dto.getStageId()));

        event.setHomeTeam(teamRepository.findBySlug(dto.getHomeTeamSlug())
                .orElseThrow(() -> new EntityNotFoundException("Home team not found: " + dto.getHomeTeamSlug())));

        event.setAwayTeam(teamRepository.findBySlug(dto.getAwayTeamSlug())
                .orElseThrow(() -> new EntityNotFoundException("Away team not found: " + dto.getAwayTeamSlug())));

        if (dto.getVenueId() != null) {
            event.setVenue(venueRepository.getReferenceById(dto.getVenueId()));
        } else {
            event.setVenue(null);
        }
    }

    private Event findByIdInternal(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    private void validateTeams(EventRequestDTO dto) {
        if (dto.getHomeTeamSlug() == null || dto.getAwayTeamSlug() == null) {
            throw new IllegalArgumentException("Team slugs cannot be null");
        }
        if (dto.getHomeTeamSlug().equalsIgnoreCase(dto.getAwayTeamSlug())) {
            throw new IllegalArgumentException("Home and Away team cannot be the same");
        }
    }
}
