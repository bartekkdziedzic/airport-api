package com.example.airport.mapper;

import com.example.airport.model.Departure;
import com.example.airport.model.flightradar.FlightRadarDeparture;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlightRadarResponseToDepartureMapper {

    public static List<Departure> mapFlightRadarResponseToDepartureList(List<FlightRadarDeparture> flightRadarDepartureList, String arrIata) {

        Objects.requireNonNull(flightRadarDepartureList,arrIata);

        List<Departure> departureList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (FlightRadarDeparture flightRadarDeparture : flightRadarDepartureList) {
            Departure departure = new Departure(
                    flightRadarDeparture.getDestinationIataCode(),
                    arrIata,
                    formatter.format(LocalDateTime
                            .ofInstant(Instant
                                    .ofEpochSecond(flightRadarDeparture.getScheduledDepartureTimestamp()), ZoneId.systemDefault())),
                    flightRadarDeparture.getAircraftModelCode());

            departureList.add(departure);
        }

        return departureList;
    }
}
