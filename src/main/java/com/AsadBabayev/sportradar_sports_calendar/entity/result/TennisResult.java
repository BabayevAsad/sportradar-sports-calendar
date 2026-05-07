package com.AsadBabayev.sportradar_sports_calendar.entity.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "result_tennis")
@DiscriminatorValue("TENNIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TennisResult extends Result {
    private Integer homeSets;
    private Integer awaySets;

    @ElementCollection
    @CollectionTable(name = "result_tennis_scores", joinColumns = @JoinColumn(name = "_result_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<String> setScores = new ArrayList<>();
}