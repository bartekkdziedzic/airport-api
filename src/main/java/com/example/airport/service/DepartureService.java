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

    public List<Departure> getDepartures() {
        try {
            DepartureResponse departureResponse = webClient
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("api_key", accessKey)
                            .queryParam("dep_iata", depIata)
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
            return departureResponse.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("exception caught", e);
        }
    }

//    public List<Departure> deserializeResponse(){
//        getDepartures();
//        ObjectMapper objectMapper = new ObjectMapper();
//        DepartureResponse departureResponse = objectMapper.readValue(getDepartures());
//    }

}
