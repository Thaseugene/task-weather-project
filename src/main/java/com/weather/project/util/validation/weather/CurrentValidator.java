package com.weather.project.util.validation.weather;

import com.weather.project.model.weather.api.entity.Weather;

public class CurrentValidator implements WeatherValidator {

    private WeatherValidator nextValidator;

    @Override
    public void validate(Weather weather) throws ValidationException {
        if (weather == null) {
            throw new ValidationException("Weather is required");
        } else if (weather.getCurrent() == null) {
            throw new ValidationException("Current weather is required");
        } else if (weather.getCurrent().getCondition() == null || weather.getCurrent().getTemp() == 0 || weather.getCurrent().getWindVelocity() == 0 || weather.getCurrent().getPressure() == 0 || weather.getCurrent().getHumidity() == 0) {
            throw new ValidationException("Current weather condition, temperature, wind velocity, pressure and humidity are required");
        } else if (nextValidator != null) {
            nextValidator.validate(weather);
        }
    }

    @Override
    public void setNext(WeatherValidator nextValidator) {
        this.nextValidator = nextValidator;
    }
}
