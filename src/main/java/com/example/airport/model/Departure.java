package com.example.airport.model;

import java.time.LocalDateTime;

public record Departure(

        String dep_iata,
        String arr_iata,
        LocalDateTime dep_time,
        String aircraft_icao) {

}
