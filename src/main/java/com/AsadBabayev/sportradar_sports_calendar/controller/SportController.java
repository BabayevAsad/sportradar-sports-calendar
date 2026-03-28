package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SportController {

    ResponseEntity<List<SportDto>> getAllSports();

    ResponseEntity<SportDto> getSportById(Long id);

    ResponseEntity<SportDto> createSport(SportRequestDto requestDto);

    ResponseEntity<SportDto> updateSport(SportRequestDto requestDto, Long id);

    ResponseEntity<Void> deleteSport(Long id);
}