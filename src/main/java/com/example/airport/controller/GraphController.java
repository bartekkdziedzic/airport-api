package com.example.airport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GraphController {

    @GetMapping("/graph/{depIata}")
    public String getGraph(@PathVariable("depIata") String depIata, Model model) {
        model.addAttribute("city", depIata);
        return "graph";
    }

    @GetMapping("/frgraph/{depIata}")
    public String getFrGraph(@PathVariable("depIata") String depIata, Model model) {
        model.addAttribute("city", depIata);
        return "frgraph";
    }


}
