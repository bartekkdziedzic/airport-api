package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.CalculationService;
import com.example.airport.service.DepartureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    @GetMapping("/show")
    public String showGraph(Model model) {
        List<Departure> departures = departureService.getDepartures();
        Map<LocalDateTime, Integer> sortedGraph = calculationService.calculateDepartureGraph(departures);
        Map<String, Integer> stringGraph = calculationService.convertGraphToChartable(sortedGraph);


        model.addAttribute("jsonData", stringGraph);
        return "graph";
    }

}
