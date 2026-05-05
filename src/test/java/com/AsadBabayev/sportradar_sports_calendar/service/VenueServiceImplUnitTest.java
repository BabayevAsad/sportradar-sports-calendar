package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Venue;
import com.AsadBabayev.sportradar_sports_calendar.mapper.VenueMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.VenueRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.impl.VenueServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceImplUnitTest {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private VenueMapper venueMapper;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Venue venue;
    private VenueDTO venueDto;
    private VenueRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        venue = Venue.builder()
                .id(1L)
                .name("Wembley Stadium")
                .city("London")
                .build();

        venueDto = new VenueDTO();
        requestDto = new VenueRequestDTO();
    }

    @Test
    void shouldReturnAllVenues() {
        when(venueRepository.findAll()).thenReturn(List.of(venue));
        when(venueMapper.mapToDTO(venue)).thenReturn(venueDto);

        List<VenueDTO> result = venueService.getAllVenues();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(venueDto);
        verify(venueRepository).findAll();
    }

    @Test
    void shouldReturnVenueByIdWhenIdExists() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(venueMapper.mapToDTO(venue)).thenReturn(venueDto);

        VenueDTO result = venueService.getVenueById(1L);

        assertThat(result).isNotNull();
        verify(venueRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenVenueByIdNotFound() {
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> venueService.getVenueById(1L));
    }

    @Test
    void shouldSaveVenueSuccessfully() {
        when(venueRepository.save(any(Venue.class))).thenReturn(venue);
        when(venueMapper.mapToDTO(venue)).thenReturn(venueDto);

        VenueDTO result = venueService.saveVenue(requestDto);

        assertThat(result).isNotNull();
        verify(venueMapper).updateEntityFromDto(eq(requestDto), any(Venue.class));
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    void shouldUpdateVenueSuccessfully() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
        when(venueRepository.save(venue)).thenReturn(venue);
        when(venueMapper.mapToDTO(venue)).thenReturn(venueDto);

        VenueDTO result = venueService.updateVenue(requestDto, 1L);

        assertThat(result).isNotNull();
        verify(venueMapper).updateEntityFromDto(requestDto, venue);
        verify(venueRepository).save(venue);
    }

    @Test
    void shouldDeleteVenueSuccessfully() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        venueService.deleteVenue(1L);

        verify(venueRepository).delete(venue);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentVenue() {
        when(venueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> venueService.deleteVenue(1L));
        verify(venueRepository, never()).delete(any());
    }
}