package com.example.airport.mapper;

import com.example.airport.model.Departure;
import com.example.airport.model.flightradar.FlightRadarDeparture;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FlightRadarResponseToDepartureMapperTest {

    @Test
    void mapFlightRadarResponseToDepartureList() {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<FlightRadarDeparture> testInputList = List.of(new FlightRadarDeparture(
                "test",
                "test",
                1L,
                "test"
        ));

        List<Departure> result = FlightRadarResponseToDepartureMapper.
                mapFlightRadarResponseToDepartureList(testInputList, "test");


        List<Departure> expectedResult = List.of(new Departure(
                "test",
                "test",
                formatter.format(LocalDateTime
                        .ofInstant(Instant
                                .ofEpochSecond(1L), ZoneId.systemDefault())),
                "test"
        ));


        assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    void mapDeparturesWhenNullGiven() {

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> FlightRadarResponseToDepartureMapper.mapFlightRadarResponseToDepartureList(null, null));
    }


}