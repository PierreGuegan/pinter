package com.project.pinter.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/pins")
public class PinController {

    @GetMapping
    public List<String> getPins() {
        return List.of("Pin 1", "Pin 2", "Pin 3");
    }
}