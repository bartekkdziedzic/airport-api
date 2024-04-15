package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import com.example.airport.model.Departure;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Getter
public class CalculationService {

    private final DistributionService distributionService;
    private final EnumMap<AircraftCode, Integer> aircraftCapacityMap;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CalculationService(DistributionService distributionService) {
        this.distributionService = distributionService;
        this.aircraftCapacityMap = getAircraftCapacityMap();
    }


    public Map<LocalDateTime, Integer> calculateDeparturesGraph(List<Departure> departures) { //sum passenger Integer values corresponding to timestamp

        TreeMap<LocalDateTime, Integer> graph = new TreeMap<>();

        for (Departure departure : departures
        ) {
            // get distribution map for every departure
            calculateSingleDepartureDistribution(departure)
                    // sum current map values with graph values
                    .forEach((key, value) -> graph.merge(key, value, Integer::sum));
        }
        // return sorted graph
        return graph;
    }

    public Map<LocalDateTime, Integer> calculateSingleDepartureDistribution(Departure departure) {

        //use distribution adaptation method
        Map<LocalDateTime, Integer> adaptedDistributionMap = distributionService.adaptDistribution(LocalDateTime.parse(departure.getDep_time(), formatter));
        //use getPassengerAmount
        Integer passengerAmount = distributionService.getPassengerAmount(departure);

        //create dedicatedDistributionMap by modifying adapted distribution
        return adaptedDistributionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        key -> key.getValue() * passengerAmount / 100
                ));
    }

}

