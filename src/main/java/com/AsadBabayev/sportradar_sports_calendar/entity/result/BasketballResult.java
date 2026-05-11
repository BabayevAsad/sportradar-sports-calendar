package com.AsadBabayev.sportradar_sports_calendar.entity.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "result_basketball")
@DiscriminatorValue("BASKETBALL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BasketballResult extends Result {
    private Integer homeTotalPoints;
    private Integer awayTotalPoints;

    @ElementCollection
    @CollectionTable(name = "result_basketball_quarters", joinColumns = @JoinColumn(name = "_result_id"))
    private List<Integer> quarterPoints = new ArrayList<>();
}