package com.example.airport.httpservice;


import com.example.airport.model.Departure;
import com.example.airport.model.DepartureResponse;
import com.example.airport.model.flightradar.Schedule;
import com.example.airport.service.DatabaseService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
public class DepartureService {

    private final WebClient.Builder webClient;
    private final DatabaseService databaseService;

    public DepartureService(WebClient.Builder webClient, DatabaseService databaseService) {
        this.webClient = webClient;
        this.databaseService = databaseService;
    }

    private final static String baseUrl = "https://airlabs.co/api/v9/schedules";
    private final static String accessKey = "3532cddd-d3b6-4ab7-b19c-863ce43991b3";
    private int offset = 0; // Initialize offset to 0


    public List<Departure> getDepartures(String depIata) {
        try {
            DepartureResponse departureResponse = webClient
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("api_key", accessKey)
                            .queryParam("dep_iata", depIata)
                            .queryParam("offset", offset)  // Include the offset parameter
                            .build())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new Exception("wrong query"))
                    )
                    .bodyToMono(new ParameterizedTypeReference<DepartureResponse>() {
                    })
                    .blockOptional()
                    .orElseThrow(() -> new RuntimeException("body is null"));
            List<Departure> departures = departureResponse.response();

            // Check if there is more data available
            boolean hasMore = departureResponse.request().has_more();
            if (hasMore) {
                offset += departures.size();
            }
            // saveDeparturesToDatabase(departures);
            databaseService.saveDepartures(departures);
            return departures;
        } catch (Exception e) {
            throw new RuntimeException("exception caught", e);
        }
    }

    private final static String baseFRUrl = "https://api.flightradar24.com/common/v1/airport.json";

    public Schedule getFlightRadarDepartures(String airportCode) {
        try {
            long timestamp = Instant.now().getEpochSecond();

            Schedule schedule = webClient
                    .baseUrl(baseFRUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("code", airportCode)
                            .queryParam("plugin[]", "")
                            .queryParam("plugin-setting[schedule][mode]", "departures")
                            .queryParam("plugin-setting[schedule][timestamp]", timestamp)
                            .queryParam("page", 1)
                            .queryParam("limit", 100)
                            .queryParam("fleet", "")
                            .queryParam("token", "")
                            .build())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new Exception("wrong query"))
                    )
                    .bodyToMono(Schedule.class)
                    .blockOptional()
                    .orElseThrow(() -> new RuntimeException("body is null"));

            // Save departures to the database


            return schedule;
        } catch (Exception e) {
            throw new RuntimeException("exception caught", e);
        }
    }


}
