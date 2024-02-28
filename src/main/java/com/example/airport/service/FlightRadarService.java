package com.example.airport.service;

import com.example.airport.model.flightradar.FlightField;
import com.example.airport.model.flightradar.FlightRadarDeparture;
import com.example.airport.model.flightradar.ResponseFR;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightRadarService {


    public List<FlightRadarDeparture> getFlightRadarDepartureList(ResponseFR pluginData, String arrIata) {
        List<FlightRadarDeparture> flightRadarDepartureList = new ArrayList<>();
        List<FlightField> flights = pluginData.getResult().getResponse().getAirport().getPluginData().getSchedule().getDepartures().getData();

        for (FlightField flight : flights
        ) {
            FlightRadarDeparture flightRadarDeparture = new FlightRadarDeparture(
                    arrIata,
                    flight.getFlight().getAirport().getDestination().getCode().getIata(),
                    flight.getFlight().getTime().getScheduled().getDeparture(),
                    flight.getFlight().getAircraft().getModel().getCode()
            );
            flightRadarDepartureList.add(flightRadarDeparture);

        }
        return flightRadarDepartureList;
    }


}
