package com.AsadBabayev.sportradar_sports_calendar.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stage extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("ordering")
    @Column(name = "ordering")
    private Integer ordering;
}
