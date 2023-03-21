package com.weather.project.util.validation.weather;

import com.weather.project.model.weather.api.entity.Weather;

public class LocationValidator implements WeatherValidator {

    private WeatherValidator nextValidator;

    @Override
    public void validate(Weather weather) throws ValidationException {
        if (weather == null) {
            throw new ValidationException("Weather is required");
        } else if (weather.getLocation() == null) {
            throw new ValidationException("Location is required.");
        } else if (weather.getLocation().getRegion() == null || weather.getLocation().getLocalTime() == null) {
            throw new ValidationException("Location region and local time are required.");
        } else if (nextValidator != null) {
            nextValidator.validate(weather);
        }
    }

    @Override
    public void setNext(WeatherValidator nextValidator) {
        this.nextValidator = nextValidator;
    }
}
