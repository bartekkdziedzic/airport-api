package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import com.example.airport.model.Departure;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
@Getter
public class DistributionService {

    private final EnumMap<AircraftCode, Integer> aircraftCapacityMap;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DistributionService() {
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
        enumMap.put(AircraftCode.UNKNOWN, 150);

        return enumMap;
    }

    private HashMap<LocalDateTime, Integer> getLongestDistributionMap(LocalDateTime time) {
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

        return longestDistributionMap;
    }


    private HashMap<LocalDateTime, Integer> getShortestDistributionMap(LocalDateTime time) {
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

        return shortestDistributionMap;
    }

    public TreeMap<LocalDateTime, Integer> interpolate(HashMap<LocalDateTime, Integer> map) {
        TreeMap<LocalDateTime, Integer> sortedMap = new TreeMap<>(map);
        TreeMap<LocalDateTime, Integer> interpolatedMap = new TreeMap<>();

        Map.Entry<LocalDateTime, Integer> previousEntry = null;
        for (Map.Entry<LocalDateTime, Integer> entry : sortedMap.entrySet()) {
            if (previousEntry != null) {
                LocalDateTime previousTime = previousEntry.getKey();
                int previousValue = previousEntry.getValue();
                LocalDateTime currentTime = entry.getKey();
                int currentValue = entry.getValue();

                int minutesDiff = (int) java.time.Duration.between(previousTime, currentTime).toMinutes();
                int valueDiff = currentValue - previousValue;

                for (int i = 0; i < minutesDiff; i += 5) {
                    LocalDateTime interpolatedTime = previousTime.plusMinutes(i);
                    int interpolatedValue = previousValue + (valueDiff * i) / minutesDiff;
                    interpolatedMap.put(interpolatedTime, interpolatedValue);
                }
            }
            previousEntry = entry;
        }
        interpolatedMap.put(previousEntry.getKey(), previousEntry.getValue());

        return interpolatedMap;
    }


    public Map<LocalDateTime, Integer> adaptDistribution(LocalDateTime time) {

        // longest boundary distribution // 22:00 - 04:00 distribution
        HashMap<LocalDateTime, Integer> longestDistributionMap = getLongestDistributionMap(time);


        // shortest boundary distribution // 04:00 - 06:00 distribution
        HashMap<LocalDateTime, Integer> shortestDistributionMap = getShortestDistributionMap(time);

        //define conditions for boundary distributions
        if (time.getHour() >= 4 && time.getHour() <= 6) {
            return interpolate(shortestDistributionMap);
        } else if (time.getHour() > 21 || time.getHour() < 5) {
            return interpolate(longestDistributionMap);

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
            return interpolate(result);
        }
    }

    public Integer getPassengerAmount(Departure departure) { //possible redirect to double/float
        if (AircraftCode.contains(departure.aircraft_icao()))
            try {
                return (int) (aircraftCapacityMap.get(AircraftCode.valueOf(departure.aircraft_icao())) * 0.9);
            } catch (NullPointerException nullPointerException) {
                return (int) (aircraftCapacityMap.get(AircraftCode.UNKNOWN) * 0.9); //default value when api provides no aircraft model
            }
        else {
            return 140; //default value when api provides aircraft model that is not on EnumMap
        }
    }

}
