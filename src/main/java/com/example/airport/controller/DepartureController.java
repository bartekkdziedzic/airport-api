package com.example.airport.controller;

import com.example.airport.model.Response;
import com.example.airport.service.DepartureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartureController {

    private DepartureService departureService;

    public DepartureController(DepartureService departureService) {
        this.departureService = departureService;
    }


    @GetMapping(value = "")
    public Response getData() {

        return departureService.getDepartures();
    }

}
