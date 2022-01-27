package com.bwap.weatherapp.WeatherApp.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class WeatherService {

    public static final String OPEN_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final OkHttpClient okHttpClient;
    private final CurrentLocationService currentLocationService;
    private static final String OPEN_WEATHER_API_KEY = "39344779026a327572d6125dd1766b77";
    private String city;
    private String unit;

    public WeatherService(CurrentLocationService currentLocationService) {
        this.currentLocationService = currentLocationService;
        this.okHttpClient = new OkHttpClient();
    }

    public JSONObject getWeather() {
        Request request = new Request.Builder()
                .url(OPEN_WEATHER_API + this.getCity() + "&units="
                        + this.getUnit() + "&appid=" + OPEN_WEATHER_API_KEY)
                .build();
        try {
            Response response = this.okHttpClient.newCall(request).execute();
            return new JSONObject(Objects.requireNonNull(response.body()).string());
        } catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public JSONArray returnWeatherArray() throws JSONException {
        return this.getWeather().getJSONArray("weather");
    }

    public JSONObject returnMain() throws JSONException {
        return getWeather().getJSONObject("main");
    }

    public JSONObject returnWind() throws JSONException {
        return getWeather().getJSONObject("wind");
    }

    public JSONObject returnSys() throws JSONException {
        return getWeather().getJSONObject("sys");
    }

    public String getCity() {
        return city == null ? this.currentLocationService.getCurrentLocation().getCity() : city;
    }

    public void setCity(String city) {
        if(Objects.equals(city, "")) {
          this.city = this.currentLocationService.getCurrentLocation().getCity();
        } else {
            this.city = city;
        }
    }

    public String getUnit() {
        return unit == null ? "metric" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
