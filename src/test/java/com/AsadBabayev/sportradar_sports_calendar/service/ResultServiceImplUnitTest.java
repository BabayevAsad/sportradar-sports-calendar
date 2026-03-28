package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.FootballResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.TennisResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.FootballResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.TennisResultRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Competition;
import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import com.AsadBabayev.sportradar_sports_calendar.entity.Sport;
import com.AsadBabayev.sportradar_sports_calendar.entity.SportType;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.FootballResult;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.Result;
import com.AsadBabayev.sportradar_sports_calendar.entity.result.TennisResult;
import com.AsadBabayev.sportradar_sports_calendar.mapper.ResultMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.EventRepository;
import com.AsadBabayev.sportradar_sports_calendar.repository.ResultRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.ResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplUnitTest {

    @Mock private ResultRepository resultRepository;
    @Mock private EventRepository eventRepository;
    @Mock private ResultMapper resultMapper;

    @InjectMocks
    private ResultServiceImpl resultService;

    private Event event;
    private FootballResult footballResult;
    private FootballResultRequestDTO footballRequest;

    @BeforeEach
    void setUp() {
        Sport sport = new Sport();
        sport.setName(SportType.FOOTBALL);

        Competition competition = new Competition();
        competition.setSport(sport);

        event = new Event();
        event.setId(1L);
        event.setCompetition(competition);

        footballResult = new FootballResult();
        footballResult.setId(10L);
        footballResult.setEvent(event);

        footballRequest = new FootballResultRequestDTO();
        footballRequest.setEventId(1L);
        footballRequest.setWinner("Home Team");
    }

    @Test
    void shouldSaveResultSuccessfully() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.empty());
        when(resultMapper.createEntityBySport(SportType.FOOTBALL)).thenReturn(footballResult);
        when(resultRepository.save(any(Result.class))).thenReturn(footballResult);

        when(resultMapper.toDTO(any(Result.class))).thenReturn(new FootballResultDTO());

        ResultDTO result = resultService.saveResult(footballRequest);

        assertThat(result).isNotNull();
        verify(resultRepository).save(any(Result.class));
        verify(eventRepository).save(event);
    }

    @Test
    void shouldUpdateResultSuccessfully() {
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.of(footballResult));
        when(resultRepository.save(any(Result.class))).thenReturn(footballResult);

        when(resultMapper.toDTO(any(Result.class))).thenReturn(new FootballResultDTO());

        ResultDTO result = resultService.updateResult(footballRequest, 1L);

        assertThat(result).isNotNull();
        verify(resultMapper).updateEntityFromDto(eq(footballRequest), eq(footballResult));
        verify(resultRepository).save(footballResult);
    }

    @Test
    void shouldThrowExceptionWhenResultAlreadyExists() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.of(footballResult));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> resultService.saveResult(footballRequest));

        assertThat(exception.getMessage()).contains("Conflict: A result already exists");
        verify(resultRepository, never()).save(any());
    }

    @Test
    void shouldDeleteResultAndClearEventReference() {
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.of(footballResult));

        resultService.deleteResult(1L);

        assertThat(event.getResult()).isNull();
        verify(eventRepository).save(event);
        verify(resultRepository).delete(footballResult);
    }

    @Test
    void shouldGetResultByEventId() {
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.of(footballResult));
        when(resultMapper.toDTO(footballResult)).thenReturn(new FootballResultDTO());

        ResultDTO result = resultService.getResultByEventId(1L);

        assertThat(result).isNotNull();
        verify(resultRepository).findByEventId(1L);
    }

    @Test
    void shouldGetAllResults() {
        when(resultRepository.findAll()).thenReturn(List.of(footballResult));
        when(resultMapper.toDTO(any(Result.class))).thenReturn(new FootballResultDTO());

        List<ResultDTO> results = resultService.getAllResults();

        assertThat(results).hasSize(1);
        verify(resultRepository).findAll();
    }

    @Test
    void shouldThrowExceptionWhenEventNotFoundInSave() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> resultService.saveResult(footballRequest));

        assertThat(exception.getMessage()).isEqualTo("Event not found");
    }

    @Test
    void shouldThrowExceptionWhenResultNotFoundByEventId() {
        when(resultRepository.findByEventId(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> resultService.getResultByEventId(99L));

        assertThat(exception.getMessage()).contains("Result not found for eventId");
    }

    @Test
    void shouldHandleTennisResultSuccessfully() {
        Sport tennisSport = new Sport();
        tennisSport.setName(SportType.TENNIS);
        event.getCompetition().setSport(tennisSport);

        TennisResult tennisResult = new TennisResult();
        TennisResultRequestDTO tennisRequest = new TennisResultRequestDTO();
        tennisRequest.setEventId(1L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(resultRepository.findByEventId(1L)).thenReturn(Optional.empty());
        when(resultMapper.createEntityBySport(SportType.TENNIS)).thenReturn(tennisResult);
        when(resultRepository.save(any(Result.class))).thenReturn(tennisResult);
        when(resultMapper.toDTO(any(Result.class))).thenReturn(new TennisResultDTO());

        ResultDTO result = resultService.saveResult(tennisRequest);

        assertThat(result).isInstanceOf(TennisResultDTO.class);
        verify(resultMapper).createEntityBySport(SportType.TENNIS);
    }
}