package com.example.airport.controller;

import com.example.airport.service.CalculationService;
import com.example.airport.service.DepartureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartureController.class)
class DepartureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartureService departureService;

    @MockBean
    private CalculationService calculationService;

    @Test
    public void endpointShouldReturnNotFound() throws Exception {

        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Departures not found");
        when(departureService.getDepartures(anyString())).thenThrow(exception);

        this.mockMvc.perform(get("/api/fly/TEST_USER"))
                .andExpect(status().isNotFound());
    }
}