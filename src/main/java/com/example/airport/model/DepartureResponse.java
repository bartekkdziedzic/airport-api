package com.example.airport.model;

import lombok.Getter;

import java.util.List;

@Getter
public class DepartureResponse {

    private List<Departure> response;
    private Request request;
}