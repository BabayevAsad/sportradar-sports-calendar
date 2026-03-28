package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.SportController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportDto;
import com.AsadBabayev.sportradar_sports_calendar.dto.Sport.SportRequestDto;
import com.AsadBabayev.sportradar_sports_calendar.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/sports")
@RequiredArgsConstructor
public class SportControllerImpl implements SportController {

    private final SportService sportService;

    @GetMapping
    @Override
    public ResponseEntity<List<SportDto>> getAllSports() {
        return ResponseEntity.ok(sportService.getAllSports());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<SportDto> getSportById(@PathVariable Long id) {
        return ResponseEntity.ok(sportService.getSportById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<SportDto> createSport(@Valid @RequestBody SportRequestDto requestDto) {
        return ResponseEntity.status(201).body(sportService.saveSport(requestDto));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<SportDto> updateSport(@Valid @RequestBody SportRequestDto requestDto,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(sportService.updateSport(requestDto, id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteSport(@PathVariable Long id) {
        sportService.deleteSport(id);
        return ResponseEntity.noContent().build();
    }
}