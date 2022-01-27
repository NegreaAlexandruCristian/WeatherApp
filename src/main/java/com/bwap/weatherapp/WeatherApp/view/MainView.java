package com.bwap.weatherapp.WeatherApp.view;

import com.bwap.weatherapp.WeatherApp.service.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@SpringUI(path = "")
public class MainView extends UI {

    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityFieldText;
    private Button searchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemperature;
    private HorizontalLayout mainDescriptionLayout;
    private Label weatherDescription;
    private Label maxWeather;
    private Label minWeather;
    private Label humidity;
    private Label pressure;
    private Label wind;
    private Label feelsLike;
    private Image iconImage;

    private final WeatherService weatherService;

    public MainView(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        this.mainLayout();
        this.setHeader();
        this.setLogo();
        this.setForm();
        this.dashboardTitle();
        try {
            this.dashboardDetails();
            this.updateUI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.searchButton.addClickListener(clickEvent -> {
            if (!cityFieldText.getValue().equals("")) {
                try {
                    updateUI();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            } else {
                Notification.show("Please insert a city..");
            }
        });
    }

    private void mainLayout() {
        this.iconImage = new Image();
        this.mainLayout = new VerticalLayout();
        this.mainLayout.setWidth("100%");
        this.mainLayout.setSpacing(true);
        this.mainLayout.setMargin(true);
        this.mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(mainLayout);
    }

    private void setHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("Weather Application");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);

        header.addComponent(title);
        this.mainLayout.addComponent(header);
    }

    private void setLogo() {
        HorizontalLayout logo = new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image image = new Image(null, new ClassResource("/logo.png"));
        logo.setWidth("240px");
        logo.setHeight("240px");

        logo.addComponent(image);
        this.mainLayout.addComponent(logo);
    }

    private void setForm() {
        HorizontalLayout form = new HorizontalLayout();
        form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        form.setSpacing(true);
        form.setMargin(true);

        this.unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        this.unitSelect.setItems(items);
        this.unitSelect.setValue(items.get(0));
        form.addComponent(unitSelect);

        this.cityFieldText = new TextField();
        this.cityFieldText.setWidth("80%");
        form.addComponent(cityFieldText);

        this.searchButton = new Button();
        this.searchButton.setIcon(VaadinIcons.SEARCH);
        form.addComponent(this.searchButton);


        this.mainLayout.addComponents(form);
    }

    private void dashboardTitle() {
        this.dashboard = new HorizontalLayout();
        this.dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        location = new Label("Currently in " + weatherService.getCity());
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        this.currentTemperature = new Label("10C");
        this.currentTemperature.setStyleName(ValoTheme.LABEL_BOLD);
        this.currentTemperature.setStyleName(ValoTheme.LABEL_H1);

        this.dashboard.addComponents(this.location, this.iconImage, this.currentTemperature);
    }

    private void dashboardDetails() throws JSONException {
        this.mainDescriptionLayout = new HorizontalLayout();
        this.mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        this.weatherDescription = new Label("Description: " + this.getData()[1]);
        this.weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        descriptionLayout.addComponents(weatherDescription);

        this.minWeather = new Label("Min: " + weatherService.returnMain().getInt("temp_min"));
        descriptionLayout.addComponents(this.minWeather);

        this.maxWeather = new Label("Max: " +  weatherService.returnMain().getInt("temp_max"));
        descriptionLayout.addComponents(this.maxWeather);

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        this.pressure = new Label("Pressure: " + this.weatherService.returnMain().getInt("pressure"));
        pressureLayout.addComponents(this.pressure);

        this.humidity = new Label("Humidity: " +  this.weatherService.returnMain().getInt("humidity"));
        pressureLayout.addComponents(this.humidity);

        this.wind = new Label("Wind: " + this.weatherService.returnWind().getInt("speed"));
        pressureLayout.addComponents(this.wind);

        this.feelsLike = new Label("Feels like: " + this.weatherService.returnMain().getDouble("feels_like"));
        pressureLayout.addComponents(this.feelsLike);

        this.mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);
        this.mainLayout.addComponents(this.mainDescriptionLayout, this.dashboard, this.mainDescriptionLayout);
    }

    public String[] getData() {
        String weatherIconCode = "";
        String weatherDescriptionValue = "";
        try {
            JSONArray jsonArray = weatherService.returnWeatherArray();
            for (int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject weatherObject = jsonArray.getJSONObject(i);
                weatherIconCode = weatherObject.getString("icon");
                weatherDescriptionValue = weatherObject.getString("description");
                weatherDescriptionValue = StringUtils.capitalize(weatherDescriptionValue);
            }
            return new String[] {weatherIconCode, weatherDescriptionValue};
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private void updateUI() throws JSONException {
        String city = this.cityFieldText.getValue();
        String defaultUnit = "C";
        weatherService.setCity(city);
        if (city.equals("")) {
            city = weatherService.getCity();
        }

        if (unitSelect.getValue().equals("F")) {
            weatherService.setUnit("imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0" + "F";
        } else {
            weatherService.setUnit("metric");
            unitSelect.setValue("C");
            defaultUnit = "\u00b0" + "C";
        }

        location.setValue("Currently in " + city);
        JSONObject weatherMainObject = weatherService.returnMain();
        int temperature = weatherMainObject.getInt("temp");
        currentTemperature.setValue(temperature + defaultUnit);

        String[] data = this.getData();
        String weatherIconCode = data[0];
        String weatherDescriptionValue = data[1];


        this.iconImage.setSource(new ExternalResource("https://openweathermap.org/img/wn/"
                + weatherIconCode + "@2x.png"));
        this.weatherDescription.setValue("Description: " + weatherDescriptionValue);
        this.minWeather.setValue("Min Temp: " + weatherService.returnMain().getInt("temp_min"));
        this.maxWeather.setValue("Max Temp: " + weatherService.returnMain().getInt("temp_max"));
        this.pressure.setValue("Pressure: " + this.weatherService.returnMain().getInt("pressure"));
        this.wind.setValue("Wind: " + this.weatherService.returnWind().getInt("speed"));
        this.humidity.setValue("Humidity: " + this.weatherService.returnMain().getInt("humidity"));
        this.feelsLike.setValue("Feels like: " + this.weatherService.returnMain().getDouble("feels_like"));
        this.mainLayout.addComponents(this.dashboard, this.mainDescriptionLayout);
    }
}
