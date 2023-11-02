package com.example.airport.service;

import com.example.airport.model.Response;
import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DepartureService {

    private final WebClient.Builder webClient;

    public DepartureService(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(baseUrl);
    }

    private final static String baseUrl = "https://airlabs.co/api/v9/schedules";
    private final static String accessKey = "3532cddd-d3b6-4ab7-b19c-863ce43991b3";
    private final static String depIata = "KRK";

//    private final static String baseUrl = "http://api.aviationstack.com/v1/flights";
//    private final static String accessKey = "25af237acc98b1339de452f1298707b8";
//    private final static String depIata = "KRK";

    public Response getDepartures() {
//        String apiKey = "access_key=" + accessKey;
//        String departureIata = "dep_iata=" + depIata;

        return webClient
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
                .bodyToMono(new ParameterizedTypeReference<Response>() {
                })
                .block();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();

        //   gson.fromJson(departureService.getDepartures(),Departure)
    }

    public static void deserializeSample() {

    }


}
