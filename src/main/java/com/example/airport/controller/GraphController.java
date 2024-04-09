package com.example.airport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GraphController {

    @GetMapping("/graph")
    public String getGraph(Model model) {
        return "graph";
    }

    @GetMapping("/frgraph")
    public String getFrGraph(Model model) {
        return "frgraph";
    }

}
