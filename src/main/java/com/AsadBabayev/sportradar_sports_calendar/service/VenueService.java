package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;

import java.util.List;

public interface VenueService {

    List<VenueDTO> getAllVenues();

    VenueDTO getVenueById(Long id);

    VenueDTO saveVenue(VenueRequestDTO venueRequestDTO);

    VenueDTO updateVenue(VenueRequestDTO venueRequestDTO, Long id);

    void deleteVenue(Long id);
}
