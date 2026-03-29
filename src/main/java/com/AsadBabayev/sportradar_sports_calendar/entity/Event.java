package com.AsadBabayev.sportradar_sports_calendar.entity;

import com.AsadBabayev.sportradar_sports_calendar.entity.result.Result;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_competition_id", nullable = false)
    private Competition competition;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_stage_id", nullable = false)
    private Stage stage;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "time_utc", nullable = false)
    private LocalTime timeUtc;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_venue_id")
    private Venue venue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_home_team_id")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "_away_team_id")
    private Team awayTeam;
}