package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.VenueController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/venues")
@RequiredArgsConstructor
public class VenueControllerImpl implements VenueController {

    private final VenueService venueService;

    @GetMapping
    @Override
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(venueService.saveVenue(requestDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<VenueDTO> updateVenue(@Valid @RequestBody VenueRequestDTO requestDTO,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(venueService.updateVenue(requestDTO, id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
