package com.example.airport.model.flightradar;

import lombok.Data;

@Data
public class Flight {
    private Aircraft aircraft;
    private Airport airport;
    private Time time;
}
