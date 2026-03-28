package com.AsadBabayev.sportradar_sports_calendar.controller.impl;

import com.AsadBabayev.sportradar_sports_calendar.controller.TeamController;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Team.TeamRequestDTO;
import com.AsadBabayev.sportradar_sports_calendar.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/teams")
@RequiredArgsConstructor
public class TeamControllerImpl implements TeamController {

    private final TeamService teamService;

    @GetMapping
    @Override
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PostMapping
    @Override
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(teamService.saveTeam(requestDTO));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<TeamDTO> updateTeam(@Valid @RequestBody TeamRequestDTO requestDTO,
                                              @PathVariable Long id) {
        return ResponseEntity.ok(teamService.updateTeam(requestDTO, id));
    }

    @DeleteMapping("{id}")
    @Override
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
