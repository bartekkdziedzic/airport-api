package com.example.airport.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public record Departure(

        String dep_iata,
        String arr_iata,
        LocalDateTime dep_time,
        String aircraft_icao) {

}
