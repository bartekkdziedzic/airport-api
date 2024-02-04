package com.example.airport.service;

import com.example.airport.model.flightradar.Flight;
import com.example.airport.model.flightradar.FlightRadarDeparture;
import com.example.airport.model.flightradar.Schedule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightRadarService {


    List<FlightRadarDeparture> getFlightRadarDepartureList(Schedule schedule) {
        List<FlightRadarDeparture> flightRadarDepartureList = new ArrayList<>();
        List<Flight> flights = schedule.getDepartures().getData().getFlightList();

        for (Flight flight : flights
        ) {
            FlightRadarDeparture flightRadarDeparture = new FlightRadarDeparture(
                    flight.getAirport().getDestination().getCode().getIata(),
                    flight.getTime().getScheduled().getDeparture(),
                    flight.getAircraft().getModel().getCode()
            );
            flightRadarDepartureList.add(flightRadarDeparture);

        }
        return flightRadarDepartureList;
    }

}
