package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import com.example.airport.model.Departure;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculationService {

    DepartureService departureService;
    EnumMap<AircraftCode, Integer> aircraftCapacityMap;

    public CalculationService(DepartureService departureService) {
        this.departureService = departureService;
        this.aircraftCapacityMap = getAircraftCapacityMap();
    }

    List<Departure> departures = departureService.getDepartures();

    public final EnumMap<AircraftCode, Integer> getAircraftCapacityMap() {

        EnumMap<AircraftCode, Integer> enumMap = new EnumMap<AircraftCode, Integer>(AircraftCode.class);
        enumMap.put(AircraftCode.A320, 150);
        enumMap.put(AircraftCode.B38M, 186);
        enumMap.put(AircraftCode.E190, 106);
        enumMap.put(AircraftCode.E195, 112);

        return enumMap;
    }

    public void getGraphMap() { // based on api response timestamp extremes - create empty graph table ('formattedTime',Integer)
        LocalDateTime maxGraphTimestamp = getMaxTimestamp();
        LocalDateTime minTimestamp = getMinTimestamp();
        LocalDateTime minGraphTimestamp = getAdaptationDifferenceTimestamp(minTimestamp);

        HashMap<LocalDateTime, Integer> graphMap = new HashMap<LocalDateTime, Integer>();
        LocalDateTime iterationTimestamp = minGraphTimestamp;
        while (minGraphTimestamp.isBefore(maxGraphTimestamp)) {
            graphMap.put(iterationTimestamp, 0);
            iterationTimestamp = iterationTimestamp.plusMinutes(1);
        }
    }


    private void recalculateDepartureGraph(Departure departure) { //sum passenger Integer values corresponding to timestamp

        //use calculateDingleDeparture
        //distribute passengers on main graph
        //  map2.forEach((key, value) -> map1.merge(key, value, Integer::sum));


    }


    private void calculateSingleDeparture(Departure departure) {

        //use distribution adaptation method
        HashMap<LocalDateTime, Integer> adaptedDistributionMap = adaptDistribution(departure.getDep_time());
        //use getPassengerAmount
        Integer passengerAmount = getPassengerAmount(departure);

        //create dedicatedDistributionMap by modifying adapted distribution
        Map<LocalDateTime, Integer> dedicatedDistributionMap = adaptedDistributionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        key -> key.getValue() * passengerAmount
                ));

    }


    private HashMap<LocalDateTime, Integer> adaptDistribution(LocalDateTime time) {

        // longest boundary distribution
        HashMap<LocalDateTime, Integer> longestDistributionMap = new HashMap<LocalDateTime, Integer>();
        longestDistributionMap.put(time, 0);
        longestDistributionMap.put(time.minusMinutes(15), 0);
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
        longestDistributionMap.put(time.minusMinutes(225), 1); // all values divide by 100
        
        // shortest boundary distribution
        HashMap<LocalDateTime, Integer> shortestDistributionMap = new HashMap<LocalDateTime, Integer>();
        shortestDistributionMap.put(time, 0);
        shortestDistributionMap.put(time.minusMinutes(15), 0);
        shortestDistributionMap.put(time.minusMinutes(30), 2);
        shortestDistributionMap.put(time.minusMinutes(45), 10);
        shortestDistributionMap.put(time.minusMinutes(60), 17);
        shortestDistributionMap.put(time.minusMinutes(75), 30);
        shortestDistributionMap.put(time.minusMinutes(90), 26);
        shortestDistributionMap.put(time.minusMinutes(105), 10);
        shortestDistributionMap.put(time.minusMinutes(120), 4);
        shortestDistributionMap.put(time.minusMinutes(135), 1); // all values divide by 100


        //define distribution adaptation method


        return shortestDistributionMap; //distribution example taken
    }

    private LocalDateTime getMaxTimestamp() { //get latest timestamp from response
        return departures.stream()
                .map(Departure::getDep_time)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private LocalDateTime getMinTimestamp() { //get earliest timestamp from response
        return departures.stream()
                .map(Departure::getDep_time)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private LocalDateTime getAdaptationDifferenceTimestamp(LocalDateTime minTimestamp) { //get earliest timestamp from response
        // based on hour passengers tend to arrive with different notice
        // adaptation logic
        return minTimestamp.minusHours(2); //estimation for development process
    }

    private Integer getPassengerAmount(Departure departure) { //possible redirect to double/float
        try {
            return aircraftCapacityMap.get(departure.getAircraft_icao());
        } catch (NullPointerException nullPointerException) {
            return 150; //default value when api provides no aircraft model
        }
    }

}
