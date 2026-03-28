package com.AsadBabayev.sportradar_sports_calendar.service.impl;

import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Venue.VenueRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.entity.Venue;
import com.AsadBabayev.sportradar_sports_calendar.mapper.VenueMapper;
import com.AsadBabayev.sportradar_sports_calendar.repository.VenueRepository;
import com.AsadBabayev.sportradar_sports_calendar.service.VenueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    @Transactional(readOnly = true)
    @Override
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public VenueDTO getVenueById(Long id) {
        return venueMapper.mapToDTO(findByIdInternal(id));
    }

    @Transactional
    @Override
    public VenueDTO saveVenue(VenueRequestDTO dto) {

        Venue venue = new Venue();

        venueMapper.updateEntityFromDto(dto, venue);

        return venueMapper.mapToDTO(venueRepository.save(venue));
    }

    @Transactional
    @Override
    public VenueDTO updateVenue(VenueRequestDTO dto, Long id) {
        Venue venue = findByIdInternal(id);

        venueMapper.updateEntityFromDto(dto, venue);

        return venueMapper.mapToDTO(venueRepository.save(venue));
    }

    @Transactional
    @Override
    public void deleteVenue(Long id) {
        Venue venue = findByIdInternal(id);

        venueRepository.delete(venue);
    }

    private Venue findByIdInternal(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));
    }
}
