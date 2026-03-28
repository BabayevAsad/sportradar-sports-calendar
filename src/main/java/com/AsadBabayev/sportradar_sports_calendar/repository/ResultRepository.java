package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.result.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByEventId(Long eventId);
}
