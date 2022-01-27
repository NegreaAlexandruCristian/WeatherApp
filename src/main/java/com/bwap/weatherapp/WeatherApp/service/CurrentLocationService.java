package com.bwap.weatherapp.WeatherApp.service;

import com.bwap.weatherapp.WeatherApp.service.dao.CurrentLocation;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrentLocationService {

    private final RestTemplate restTemplate;
    private static final String IP_FIND_API = "https://api.ipfind.com/me?auth=";
    private static final String IP_FIND_API_KEY = "0f1093f5-a30f-42fd-b5b3-19a03597785c";

    public CurrentLocationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CurrentLocation getCurrentLocation() {
        return this.restTemplate.getForObject(IP_FIND_API + IP_FIND_API_KEY, CurrentLocation.class, 1);
    }
}
