package com.example.airport.model.flightradar;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Departures {

    private List<FlightField> data;

}
