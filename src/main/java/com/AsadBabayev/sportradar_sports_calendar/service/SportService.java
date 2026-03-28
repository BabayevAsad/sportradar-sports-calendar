package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;

import java.util.List;

public interface SportService {

    List<SportDto> getAllSports();

    SportDto getSportById(Long id);

    SportDto saveSport(SportRequestDto sportRequestDto);

    SportDto updateSport(SportRequestDto sportRequestDto, Long id);

    void deleteSport(Long id);
}