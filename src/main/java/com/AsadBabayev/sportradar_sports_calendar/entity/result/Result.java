package com.AsadBabayev.sportradar_sports_calendar.entity.result;

import com.AsadBabayev.sportradar_sports_calendar.entity.BaseEntity;
import com.AsadBabayev.sportradar_sports_calendar.entity.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "result")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Result extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "_event_id", nullable = true)
    private Event event;

    @Column(name = "winner")
    private String winner;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
}