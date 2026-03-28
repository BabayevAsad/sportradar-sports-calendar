package com.AsadBabayev.sportradar_sports_calendar.controller;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.ResultRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ResultController {

    ResponseEntity<List<ResultDTO>> getAllResults();

    ResponseEntity<ResultDTO> getResultByEventId( Long eventId);

    ResponseEntity<ResultDTO> createResult(ResultRequestDTO requestDTO);

    ResponseEntity<ResultDTO> updateResult(ResultRequestDTO requestDTO, Long eventId);

    ResponseEntity<Void> deleteResult(Long eventId);
}
