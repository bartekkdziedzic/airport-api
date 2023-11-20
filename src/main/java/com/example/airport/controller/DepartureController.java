package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.CalculationService;
import com.example.airport.service.DepartureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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

    @GetMapping("/graph")
    public HashMap<LocalDateTime, Integer> getGraph() {

        return calculationService.calculateDepartureGraph(calculationService.getDepartures());
    }
}
