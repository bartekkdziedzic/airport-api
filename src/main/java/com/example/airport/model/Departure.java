package com.example.airport.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Departure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String dep_iata;
    String arr_iata;
    String dep_time;
    String aircraft_icao;

    public Departure(String dep_iata, String arr_iata, String dep_time, String aircraft_icao) {
        this.dep_iata = dep_iata;
        this.arr_iata = arr_iata;
        this.dep_time = dep_time;
        this.aircraft_icao = aircraft_icao;
    }
}
