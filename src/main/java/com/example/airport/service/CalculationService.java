package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import com.example.airport.model.Departure;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class CalculationService {

    private final DepartureService departureService;
    private final EnumMap<AircraftCode, Integer> aircraftCapacityMap;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CalculationService(DepartureService departureService) {
        this.departureService = departureService;
        this.aircraftCapacityMap = getAircraftCapacityMap();
    }


    public final EnumMap<AircraftCode, Integer> getAircraftCapacityMap() {

        EnumMap<AircraftCode, Integer> enumMap = new EnumMap<>(AircraftCode.class);
        enumMap.put(AircraftCode.A20N, 160);
        enumMap.put(AircraftCode.A320, 150);
        enumMap.put(AircraftCode.B738, 165);
        enumMap.put(AircraftCode.B38M, 186);
        enumMap.put(AircraftCode.DH8D, 39);
        enumMap.put(AircraftCode.E75L, 82);
        enumMap.put(AircraftCode.E190, 106);
        enumMap.put(AircraftCode.E195, 112);

        return enumMap;
    }

    public Map<LocalDateTime, Integer> calculateDepartureGraph(List<Departure> departures) { //sum passenger Integer values corresponding to timestamp

        HashMap<LocalDateTime, Integer> graph = new HashMap<>();

        for (Departure departure : departures
        ) {
            // get distribution map for every departure
            Map<LocalDateTime, Integer> singleDistribution = calculateSingleDeparture(departure);
            // sum current map values with graph values
            singleDistribution.forEach((key, value) -> graph.merge(key, value, Integer::sum));
        }
        Map<LocalDateTime, Integer> sortedGraph = new TreeMap<>(graph);
        return sortedGraph;
    }


    private Map<LocalDateTime, Integer> calculateSingleDeparture(Departure departure) {

        //use distribution adaptation method
        HashMap<LocalDateTime, Integer> adaptedDistributionMap = adaptDistribution(LocalDateTime.parse(departure.dep_time(), formatter));
        //use getPassengerAmount
        Integer passengerAmount = getPassengerAmount(departure);

        //create dedicatedDistributionMap by modifying adapted distribution
        Map<LocalDateTime, Integer> dedicatedDistributionMap = adaptedDistributionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        key -> key.getValue() * passengerAmount / 100
                ));
        return dedicatedDistributionMap;
    }


    private HashMap<LocalDateTime, Integer> adaptDistribution(LocalDateTime time) {

        // longest boundary distribution // 22:00 - 04:00 distribution
        HashMap<LocalDateTime, Integer> longestDistributionMap = new HashMap<>();
        longestDistributionMap.put(time.minusMinutes(30), 2);
        longestDistributionMap.put(time.minusMinutes(45), 8);
        longestDistributionMap.put(time.minusMinutes(60), 17);
        longestDistributionMap.put(time.minusMinutes(75), 26);
        longestDistributionMap.put(time.minusMinutes(90), 28);
        longestDistributionMap.put(time.minusMinutes(105), 27);
        longestDistributionMap.put(time.minusMinutes(120), 20);
        longestDistributionMap.put(time.minusMinutes(135), 13);
        longestDistributionMap.put(time.minusMinutes(150), 9);
        longestDistributionMap.put(time.minusMinutes(165), 7);
        longestDistributionMap.put(time.minusMinutes(180), 5);
        longestDistributionMap.put(time.minusMinutes(195), 5);
        longestDistributionMap.put(time.minusMinutes(210), 2);
        longestDistributionMap.put(time.minusMinutes(225), 1);

        // shortest boundary distribution // 04:00 - 06:00 distribution
        HashMap<LocalDateTime, Integer> shortestDistributionMap = new HashMap<>();
        shortestDistributionMap.put(time.minusMinutes(30), 2);
        shortestDistributionMap.put(time.minusMinutes(45), 10);
        shortestDistributionMap.put(time.minusMinutes(60), 17);
        shortestDistributionMap.put(time.minusMinutes(75), 30);
        shortestDistributionMap.put(time.minusMinutes(90), 26);
        shortestDistributionMap.put(time.minusMinutes(105), 10);
        shortestDistributionMap.put(time.minusMinutes(120), 4);
        shortestDistributionMap.put(time.minusMinutes(135), 1);
        shortestDistributionMap.put(time.minusMinutes(150), 0);
        shortestDistributionMap.put(time.minusMinutes(165), 0);
        shortestDistributionMap.put(time.minusMinutes(180), 0);
        shortestDistributionMap.put(time.minusMinutes(195), 0);
        shortestDistributionMap.put(time.minusMinutes(210), 0);
        shortestDistributionMap.put(time.minusMinutes(225), 0);

        //define conditions for boundary distributions
        if (time.getHour() >= 4 && time.getHour() <= 6) {
            return shortestDistributionMap;
        } else if (time.getHour() > 21 || time.getHour() < 5) {
            return longestDistributionMap;
        } else { // adaptation function
            int adaptationOffset = time.getHour() - 6;
            HashMap<LocalDateTime, Integer> result = new HashMap<>();
            Double weightFactor = (double) adaptationOffset / 16;
            for (LocalDateTime key : shortestDistributionMap.keySet()
            ) {
                // multiply each distribution map by
                result.put(key, (int) (shortestDistributionMap.get(key) * weightFactor
                        + longestDistributionMap.get(key) * (1 - weightFactor)));
            }
            return result;
        }
    }

    private Integer getPassengerAmount(Departure departure) { //possible redirect to double/float
        if (AircraftCode.contains(departure.aircraft_icao()))
            try {
                return aircraftCapacityMap.get(AircraftCode.valueOf(departure.aircraft_icao()));
            } catch (NullPointerException nullPointerException) {
                return 150; //default value when api provides no aircraft model
            }
        else {
            return 150; //default value when api provides aircraft model that is not on EnumMap
        }
    }
}

