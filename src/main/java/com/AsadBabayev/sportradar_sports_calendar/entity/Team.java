package com.AsadBabayev.sportradar_sports_calendar.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @JsonProperty("official_name")
    @Column(name = "official_name")
    private String officialName;

    @JsonProperty("slug")
    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @JsonProperty("abbreviation")
    @Column(name = "abbreviation")
    private String abbreviation;

    @JsonProperty("country_code")
    @Column(name = "country_code")
    private String countryCode;

    @JsonProperty("stage_position")
    @Column(name = "stage_position")
    private Integer stagePosition;
}
