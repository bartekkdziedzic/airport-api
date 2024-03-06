package com.example.airport.model.flightradar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FlightRadarDeparture {

    private String arrIata;
    private String destinationIataCode;
    private long scheduledDepartureTimestamp;
    private String aircraftModelCode;

}