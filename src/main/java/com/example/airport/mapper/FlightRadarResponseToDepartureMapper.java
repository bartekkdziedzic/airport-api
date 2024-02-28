package com.example.airport.mapper;

import com.example.airport.model.Departure;
import com.example.airport.model.flightradar.FlightRadarDeparture;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class FlightRadarResponseToDepartureMapper {

    public List<Departure> mapFlightRadarResponseToDepartureList(List<FlightRadarDeparture> flightRadarDepartureList, String arrIata) {

        List<Departure> departureList = new ArrayList<>();

        for (FlightRadarDeparture flightRadarDeparture : flightRadarDepartureList) {
            Departure departure = new Departure(
                    flightRadarDeparture.getDestinationIataCode(),
                    arrIata,
                    LocalDateTime
                            .ofInstant(Instant
                                    .ofEpochSecond(flightRadarDeparture.getScheduledDepartureTimestamp()), ZoneId.systemDefault())
                            .toString()
                    ,
                    flightRadarDeparture.getAircraftModelCode());

            departureList.add(departure);
        }

        return departureList;
    }
}
