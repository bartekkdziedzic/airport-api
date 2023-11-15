package com.example.airport.controller;

import com.example.airport.model.Departure;
import com.example.airport.service.DepartureService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepartureController {

    private final DepartureService departureService;

    public DepartureController(DepartureService departureService) {
        this.departureService = departureService;
    }


//   @RequestMapping("/fly")
//    public List<Departure> getData() {
//
//        return departureService.getDepartures();
//    }

}
