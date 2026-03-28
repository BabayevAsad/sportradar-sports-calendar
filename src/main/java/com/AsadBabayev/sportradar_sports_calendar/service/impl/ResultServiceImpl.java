package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.*;
import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.Result;
import com.AsadBabayev.sportradar_sports_calendar.mapper.ResultMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.EventRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.ResultRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final EventRepository eventRepository;
    private final ResultMapper resultMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ResultDTO> getAllResults() {
        return resultRepository.findAll()
                .stream()
                .map(resultMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ResultDTO getResultByEventId(final Long eventId) {
        return resultMapper.toDTO(getResultByEventIdInternal(eventId));
    }

    @Transactional
    @Override
    public ResultDTO saveResult(final ResultRequestDTO dto) {
        final Event event = getEventById(dto.getEventId());
        validateResultNotExists(dto.getEventId());

        final Result result = resultMapper.createEntityBySport(event.getCompetition().getSport().getName());
        result.setEvent(event);

        resultMapper.updateEntityFromDto(dto, result);
        final Result savedResult = resultRepository.save(result);

        event.setResult(savedResult);
        eventRepository.save(event);

        return resultMapper.toDTO(savedResult);
    }

    @Transactional
    @Override
    public ResultDTO updateResult(final ResultRequestDTO dto, final Long eventId) {
        final Result result = getResultByEventIdInternal(eventId);

        resultMapper.updateEntityFromDto(dto, result);

        return resultMapper.toDTO(resultRepository.save(result));
    }


    @Transactional
    @Override
    public void deleteResult(final Long eventId) {
        final Result result = getResultByEventIdInternal(eventId);
        final Event event = result.getEvent();

        if (event != null) {
            event.setResult(null);
            eventRepository.save(event);
        }
        resultRepository.delete(result);
    }

    private Result getResultByEventIdInternal(Long eventId) {
        return resultRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Result not found for eventId: " + eventId));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    private void validateResultNotExists(Long eventId) {
        if (resultRepository.findByEventId(eventId).isPresent()) {
            throw new RuntimeException("Conflict: A result already exists for event ID " + eventId);
        }
    }
}