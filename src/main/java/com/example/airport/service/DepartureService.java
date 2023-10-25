package com.example.airport.service;

import com.example.airport.model.Departure;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class DepartureService {

    private final WebClient.Builder webClient;

    public DepartureService(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(baseUrl);
    }

    private final static String baseUrl = "http://api.aviationstack.com/v1/flights";
    private final static String key = "?access_key=25af237acc98b1339de452f1298707b8&dep_iata=KRK";

    public List<Departure> getDepartures() {


        return webClient
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path(baseUrl + key).build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new Exception("user not found"))
                )
                .bodyToMono(new ParameterizedTypeReference<List<Departure>>() {
                })
                .block();
    }
}
