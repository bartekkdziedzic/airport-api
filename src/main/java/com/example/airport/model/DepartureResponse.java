package com.example.airport.model;

import java.util.List;

public record DepartureResponse(List<Departure> response,
                                Request request) {

}

