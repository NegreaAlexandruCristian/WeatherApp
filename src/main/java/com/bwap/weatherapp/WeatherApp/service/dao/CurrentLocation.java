package com.bwap.weatherapp.WeatherApp.service.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentLocation implements Serializable {

    public String ip_address;
    public String country;
    public String country_code;
    public String continent;
    public String continent_code;
    public String city;
    public String county;
    public String region;
    public String region_code;
    public String timezone;
    public double longitude;
    public double latitude;
    public String currency;
    public ArrayList<String> languages;

}
