package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"result", "homeTeam", "awayTeam", "competition", "stage"})
    List<Event> findAll();

    @EntityGraph(attributePaths = {"result", "homeTeam", "awayTeam", "competition", "stage"})
    List<Event> findByDate(LocalDate date);

    @EntityGraph(attributePaths = {"result", "homeTeam", "awayTeam", "competition", "stage"})
    List<Event> findByCompetition_Id(String competitionId);

    @EntityGraph(attributePaths = {"result", "homeTeam", "awayTeam", "competition", "stage"})
    Optional<Event> findById(Long id);

    @EntityGraph(attributePaths = {"result", "homeTeam", "awayTeam", "competition", "stage"})
    List<Event> findByCompetition_Sport_Name(com.AsadBabayev.sportradar_sports_calendar.entity.SportType sportName);
}
