package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.EventController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventResponseDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/api/events")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<EventResponseDTO> getAllEvents(@PageableDefault(size = 5) Pageable pageable) {

        EventResponseDTO response = eventService.getAllEvents(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping()
    @Override
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(eventService.saveEvent(requestDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<EventDTO> updateEvent(@Valid @RequestBody EventRequestDTO requestDTO,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(eventService.updateEvent(requestDTO, id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-date/{date}")
    @Override
    public ResponseEntity<List<EventDTO>> getEventsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(eventService.getEventByDate(date));
    }


    @GetMapping("/by-competition/{competitionId}")
    @Override
    public ResponseEntity<List<EventDTO>> getEventsByCompetition(@Valid @PathVariable Long competitionId) {
        return ResponseEntity.ok(eventService.getEventByCompetitionId(competitionId));
    }

    @GetMapping("/by-sport/{sport}")
    @Override
    public ResponseEntity<List<EventDTO>> getEventsBySport(@Valid @PathVariable SportType sport) {
        return ResponseEntity.ok(eventService.getEventsBySport(sport));
    }
}
