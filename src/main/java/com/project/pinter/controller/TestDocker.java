package com.project.pinter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestDocker {

    @GetMapping("/")
    public String home() {
        return "Pinter API is running 🚀";
    }
}