package com.bwap.weatherapp.WeatherApp.controller;

import com.bwap.weatherapp.WeatherApp.service.CurrentLocationService;
import com.bwap.weatherapp.WeatherApp.service.dao.CurrentLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ControllerTest {

    private final CurrentLocationService currentLocationService;

    public ControllerTest(CurrentLocationService currentLocationService) {
        this.currentLocationService = currentLocationService;
    }

    @GetMapping("")
    public CurrentLocation getCurrentLocation() {
        return this.currentLocationService.getCurrentLocation();
    }
}
