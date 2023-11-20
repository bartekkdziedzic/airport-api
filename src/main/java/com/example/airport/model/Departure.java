package com.example.airport.model;

public record Departure(

        String dep_iata,
        String arr_iata,
        String dep_time,
        String aircraft_icao) {

}
