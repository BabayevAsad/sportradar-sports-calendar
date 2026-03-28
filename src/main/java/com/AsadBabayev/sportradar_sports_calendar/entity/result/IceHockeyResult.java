package com.AsadBabayev.sportradar_sports_calendar.entity.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "result_ice_hockey")
@DiscriminatorValue("ICE_HOCKEY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class IceHockeyResult extends Result {
    private Integer homeGoals;
    private Integer awayGoals;

    @ElementCollection
    @CollectionTable(name = "result_hockey_periods", joinColumns = @JoinColumn(name = "_result_id"))
    private List<String> periodScores = new ArrayList<>();
}