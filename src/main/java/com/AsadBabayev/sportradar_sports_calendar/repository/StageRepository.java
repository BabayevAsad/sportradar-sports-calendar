package com.AsadBabayev.sportradar_sports_calendar.repository;

import com.AsadBabayev.sportradar_sports_calendar.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, String> {
}
