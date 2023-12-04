package com.example.airport.service;

import com.example.airport.enums.AircraftCode;
import com.example.airport.model.Departure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalculationServiceTest {

    @Autowired
    private CalculationService calculationService;

    @MockBean
    private DepartureService departureService;

    @Test
    void calculateDepartureGraph() throws Exception {
        //arrange
        EnumMap<AircraftCode, Integer> enumMap = new EnumMap<AircraftCode, Integer>(AircraftCode.class);
        enumMap.put(AircraftCode.A20N, 160);
        Integer passengerAmount = enumMap.get(AircraftCode.A20N);
        HashMap<LocalDateTime, Integer> testDistributionMap = new HashMap<LocalDateTime, Integer>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        testDistributionMap.put(LocalDateTime.parse("2023-12-04 10:45", formatter).minusMinutes(30), 50);
        testDistributionMap.put(LocalDateTime.parse("2023-12-04 10:45", formatter).minusMinutes(45), 50);
        Departure departure = new Departure("KRK", "EID", "2023-12-04 10:45", "A20N");
        HashMap<LocalDateTime, Integer> graph = new HashMap<>();

        //act
        Map<LocalDateTime, Integer> dedicatedDistributionMap = testDistributionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        key -> key.getValue() * passengerAmount / 100
                ));

        dedicatedDistributionMap.forEach((key, value) -> graph.merge(key, value, Integer::sum));
        Map<LocalDateTime, Integer> result = new TreeMap<>(graph);

        //assert
        Map<LocalDateTime, Integer> expectedResult = new TreeMap<>();
        expectedResult.put(LocalDateTime.parse("2023-12-04 10:45", formatter).minusMinutes(30), 80);
        expectedResult.put(LocalDateTime.parse("2023-12-04 10:45", formatter).minusMinutes(45), 80);

        assertThat(List.of(result)).isEqualTo(List.of(expectedResult));
    }
}

