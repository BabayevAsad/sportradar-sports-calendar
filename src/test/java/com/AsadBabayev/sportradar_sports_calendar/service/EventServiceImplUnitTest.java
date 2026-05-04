package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Event.EventRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.*;
import com.AsadBabayev.sportradar_sports_calendar.mapper.EventMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.*;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.EventServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplUnitTest {

    @Mock private EventRepository eventRepository;
    @Mock private CompetitionRepository competitionRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private StageRepository stageRepository;
    @Mock private VenueRepository venueRepository;
    @Mock private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventRequestDTO requestDto;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setUp() {
        homeTeam = Team.builder().id(101L).slug("home-slug").build();
        awayTeam = Team.builder().id(102L).slug("away-slug").build();

        event = Event.builder()
                .id(1L)
                .status("scheduled")
                .date(LocalDate.now().plusDays(1))
                .timeUtc(LocalTime.of(12, 0))
                .build();

        requestDto = EventRequestDTO.builder()
                .competitionId(1L)
                .stageId(1L)
                .homeTeamSlug("home-slug")
                .awayTeamSlug("away-slug")
                .venueId(50L)
                .status("scheduled")
                .date(LocalDate.now().plusDays(1))
                .timeUtc(LocalTime.of(12, 0))
                .build();
    }

    @Test
    void shouldSaveEventSuccessfully() {
        when(teamRepository.findBySlug("home-slug")).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findBySlug("away-slug")).thenReturn(Optional.of(awayTeam));

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.mapToDTO(any(Event.class))).thenReturn(new EventDTO());

        EventDTO result = eventService.saveEvent(requestDto);

        assertThat(result).isNotNull();
        verify(eventRepository).save(any(Event.class));
        verify(eventMapper).updateEntityFromDto(eq(requestDto), any(Event.class));
    }

    @Test
    void shouldThrowExceptionWhenHomeTeamNotFound() {
        when(teamRepository.findBySlug("home-slug")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.saveEvent(requestDto));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTeamsAreSame() {
        requestDto.setAwayTeamSlug("home-slug");

        assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(requestDto));
    }

    @Test
    void shouldDeleteEventSuccessfully() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.deleteEvent(1L);

        verify(eventRepository).delete(event);
    }

    @Test
    void shouldReturnEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.mapToDTO(event)).thenReturn(new EventDTO());

        EventDTO result = eventService.getEventById(1L);

        assertThat(result).isNotNull();
        verify(eventRepository).findById(1L);
    }

    @Test
    void shouldUpdateEventSuccessfully() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(teamRepository.findBySlug("home-slug")).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findBySlug("away-slug")).thenReturn(Optional.of(awayTeam));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.mapToDTO(any(Event.class))).thenReturn(new EventDTO());

        EventDTO result = eventService.updateEvent(requestDto, 1L);

        assertThat(result).isNotNull();
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    void shouldSaveEventWithoutVenueWhenVenueIdIsNull() {
        requestDto.setVenueId(null);
        when(teamRepository.findBySlug("home-slug")).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findBySlug("away-slug")).thenReturn(Optional.of(awayTeam));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.mapToDTO(any(Event.class))).thenReturn(new EventDTO());

        eventService.saveEvent(requestDto);

        verify(venueRepository, never()).getReferenceById(anyLong());
    }

    @Test
    void shouldReturnEventsByDate() {
        LocalDate date = LocalDate.now();
        when(eventRepository.findByDate(date)).thenReturn(List.of(event));
        when(eventMapper.mapToDTO(event)).thenReturn(new EventDTO());

        List<EventDTO> results = eventService.getEventByDate(date);

        assertThat(results).hasSize(1);
        verify(eventRepository).findByDate(date);
    }

    @Test
    void shouldReturnEventsByCompetitionId() {
        Long compId = 1L;
        when(eventRepository.findByCompetition_Id(compId)).thenReturn(List.of(event));
        when(eventMapper.mapToDTO(event)).thenReturn(new EventDTO());

        List<EventDTO> results = eventService.getEventByCompetitionId(compId);

        assertThat(results).hasSize(1);
        verify(eventRepository).findByCompetition_Id(compId);
    }
}