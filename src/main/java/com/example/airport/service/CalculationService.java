package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import org.springframework.stereotype.Service;

import java.util.EnumMap;

@Service
public class CalculationService {

    public EnumMap<AircraftCode, Integer> getAircraftCapacityMap() {

        EnumMap<AircraftCode, Integer> enumMap = new EnumMap<AircraftCode, Integer>(AircraftCode.class);
        enumMap.put(AircraftCode.A320, Integer.valueOf(150));
        enumMap.put(AircraftCode.B38M, Integer.valueOf(186));
        enumMap.put(AircraftCode.E190, Integer.valueOf(106));
        enumMap.put(AircraftCode.E195, Integer.valueOf(112));

        return enumMap;
    }

}
