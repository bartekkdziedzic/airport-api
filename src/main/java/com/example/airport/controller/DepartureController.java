package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.CalculationService;
import com.example.airport.service.DepartureService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DepartureController {

    private final DepartureService departureService;
    private final CalculationService calculationService;

    public DepartureController(DepartureService departureService, CalculationService calculationService) {
        this.departureService = departureService;
        this.calculationService = calculationService;
    }

    @RequestMapping("/fly")
    public List<Departure> getData() {

        return departureService.getDepartures();
    }

    @RequestMapping("/graph")
    public Map<LocalDateTime, Integer> getGraph() {
        List<Departure> departures = departureService.getDepartures();
        return calculationService.calculateDepartureGraph(departures);
    }
}
