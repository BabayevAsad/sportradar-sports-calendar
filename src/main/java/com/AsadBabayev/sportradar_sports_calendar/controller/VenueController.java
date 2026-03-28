package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VenueController {

    ResponseEntity<List<VenueDTO>> getAllVenues();

    ResponseEntity<VenueDTO> getVenueById(Long id);

    ResponseEntity<VenueDTO> createVenue(VenueRequestDTO requestDTO);

    ResponseEntity<VenueDTO> updateVenue(VenueRequestDTO requestDTO, Long id);

    ResponseEntity<Void> deleteVenue(Long id);
}
