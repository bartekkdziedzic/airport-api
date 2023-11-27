package com.example.airport.controller;

import com.example.airport.service.DepartureService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepartureControllerTest {

    @Autowired
private MockMvc mockMvc;

    @MockBean
private DepartureService departureService;



    @Test
    public void endpointShouldReturnNotFound() throws Exception {

        Exception exception = new Exception("test not found");
        when(departureService.getDepartures(anyString())).thenThrow(exception);

        String expected = Objects.requireNonNull(new JSONObject()
                        .put("status", "NOT_FOUND"))
              //  .put("message", "test not found")
                .toString();

        this.mockMvc.perform(get("/api/github/TEST_USER"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(expected));
    }


}