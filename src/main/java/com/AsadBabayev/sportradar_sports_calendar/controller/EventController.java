package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventResponseDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface EventController {

    ResponseEntity<EventResponseDTO> getAllEvents(Pageable pageable);

    ResponseEntity<EventDTO> getEventById(Long id);

    ResponseEntity<EventDTO> createEvent(EventRequestDTO requestDTO);

    ResponseEntity<EventDTO> updateEvent(EventRequestDTO requestDTO, Long id);

    ResponseEntity<Void> deleteEvent(Long id);

    ResponseEntity<List<EventDTO>> getEventsByDate(LocalDate date);

    ResponseEntity<List<EventDTO>> getEventsByCompetition(String competitionId);

    ResponseEntity<List<EventDTO>> getEventsBySport(SportType sport);
}
