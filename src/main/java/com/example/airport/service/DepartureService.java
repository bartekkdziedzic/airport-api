package com.example.airport.service;

import com.example.airport.model.Departure;
import com.example.airport.model.DepartureResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DepartureService {

    private WebClient.Builder webClient;

    public DepartureService(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(baseUrl);
    }

    private final static String baseUrl = "https://airlabs.co/api/v9/schedules";
    private final static String accessKey = "3532cddd-d3b6-4ab7-b19c-863ce43991b3";
    private final static String depIata = "KRK";
    private int offset = 0; // Initialize offset to 0

    public List<Departure> getDepartures() {
        try {
            DepartureResponse departureResponse = webClient
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
            List<Departure> departures = departureResponse.getResponse();

            // Check if there is more data available
            boolean hasMore = departureResponse.getRequest().has_more();
            if (hasMore) {
                // If there is more data, update the offset for the next request
                offset += departures.size();
                // You can make subsequent requests with the updated offset if needed
            }

            return departures;
        } catch (Exception e) {
            throw new RuntimeException("exception caught", e);
        }
    }

}
