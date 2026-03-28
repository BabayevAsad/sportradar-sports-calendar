package com.AsadBabayev.sportradar_sports_calendar.dto.Event;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private List<EventDTO> data;

    private int currentPage;
    private long totalElements;
    private int totalPages;
    private int pageSize;
    private boolean isLast;
}
