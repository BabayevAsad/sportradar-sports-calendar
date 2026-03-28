package com.AsadBabayev.sportradar_sports_calendar.service;

import com.AsadBabayev.sportradar_sports_calendar.dto.Result.ResultDTO;
import com.AsadBabayev.sportradar_sports_calendar.dto.Result.request.ResultRequestDTO;

import java.util.List;

public interface ResultService {

    List<ResultDTO> getAllResults();

    ResultDTO getResultByEventId(Long eventId);

    ResultDTO saveResult(ResultRequestDTO resultRequestDTO);

    ResultDTO updateResult(ResultRequestDTO resultRequestDTO, Long eventId);

    void deleteResult(Long eventId);
}