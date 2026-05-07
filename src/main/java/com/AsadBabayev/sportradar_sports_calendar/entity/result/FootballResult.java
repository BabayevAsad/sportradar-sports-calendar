package com.AsadBabayev.sportradar_sports_calendar.entity.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "result_football")
@DiscriminatorValue("FOOTBALL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FootballResult extends Result {

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @ElementCollection
    @CollectionTable(name = "result_football_goals", joinColumns = @JoinColumn(name = "_result_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "goal_info")
    @Builder.Default
    private List<String> goals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "result_football_yellow_cards", joinColumns = @JoinColumn(name = "_result_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "card_info")
    @Builder.Default
    private List<String> yellowCards = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "result_football_second_yellow_cards", joinColumns = @JoinColumn(name = "_result_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "card_info")
    @Builder.Default
    private List<String> secondYellowCards = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "result_football_direct_red_cards", joinColumns = @JoinColumn(name = "_result_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "card_info")
    @Builder.Default
    private List<String> directRedCards = new ArrayList<>();
}