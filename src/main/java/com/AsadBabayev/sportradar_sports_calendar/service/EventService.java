package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventResponseDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;

import java.util.List;
import java.time.LocalDate;

public interface EventService {

    EventResponseDTO getAllEvents();

    EventDTO getEventById(Long id);

    EventDTO saveEvent(EventRequestDTO eventRequestDTO);

    EventDTO updateEvent(EventRequestDTO eventRequestDTO, Long id);

    void deleteEvent(Long id);

    List<EventDTO> getEventByDate(LocalDate date);

    List<EventDTO> getEventByCompetitionId(String competitionId);

    List<EventDTO> getEventsBySport(SportType sport);
}
