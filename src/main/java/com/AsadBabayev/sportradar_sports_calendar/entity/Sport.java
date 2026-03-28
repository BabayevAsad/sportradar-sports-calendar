package com.AsadBabayev.sportradar_sports_calendar.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sport")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Sport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private SportType name;

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Competition> competitions = new ArrayList<>();
}