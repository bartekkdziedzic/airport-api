package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.DepartureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartureController {

    private DepartureService departureService;

    public DepartureController(DepartureService departureService) {
        this.departureService = departureService;
    }


    @GetMapping(value = "")
    public List<Departure> getData() {

        return departureService.getDepartures();
    }

}
