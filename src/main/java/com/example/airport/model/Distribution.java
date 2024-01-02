package com.example.airport.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Distribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String depIata;
    LocalDateTime depTime;

    @ElementCollection
    @CollectionTable(name = "distribution_entries", joinColumns = @JoinColumn(name = "distribution_id"))
    @MapKeyColumn(name = "dep_time")
    @Column(name = "count")
    Map<LocalDateTime, Integer> sortedMapDistribution;

    public Distribution(String depIata, LocalDateTime depTime, Map<LocalDateTime, Integer> sortedMapDistribution) {
        this.depIata = depIata;
        this.depTime = depTime;
        this.sortedMapDistribution = sortedMapDistribution;
    }
}
