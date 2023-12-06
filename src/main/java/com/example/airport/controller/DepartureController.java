package com.example.airport.controller;

import com.example.airport.httpservice.DepartureService;
import com.example.airport.model.Departure;
import com.example.airport.service.CalculationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/fly/{depIata}")
    public List<Departure> getData(@PathVariable("depIata") String depIata) {
        List<Departure> departures = departureService.getDepartures(depIata);

        if (departures == null || departures.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Departures not found");
        }
        return departures;
    }

    @GetMapping("/graph/{depIata}")
    public Map<LocalDateTime, Integer> getGraph(@PathVariable("depIata") String depIata) {
        List<Departure> departures = departureService.getDepartures(depIata);
        return calculationService.calculateDepartureGraph(departures);
    }

}
