package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.CalculationService;
import com.example.airport.service.DepartureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/fly/{depIata}")
    public List<Departure> getData(@PathVariable("depIata") String depIata) {

        return departureService.getDepartures(depIata);
    }

    @RequestMapping("/graph/{depIata}")
    public Map<LocalDateTime, Integer> getGraph(@PathVariable("depIata") String depIata) {
        List<Departure> departures = departureService.getDepartures(depIata);
        return calculationService.calculateDepartureGraph(departures);
    }

    @GetMapping("/show/{depIata}")
    public String showGraph(Model model, @PathVariable("depIata") String depIata) {
        List<Departure> departures = departureService.getDepartures(depIata);
        Map<LocalDateTime, Integer> sortedGraph = calculationService.calculateDepartureGraph(departures);
        Map<String, Integer> stringGraph = calculationService.convertGraphToChartable(sortedGraph);


        model.addAttribute("jsonData", stringGraph);
        return "graph";
    }

}
