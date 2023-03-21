package com.weather.project.util.validation.weather;

import com.weather.project.model.weather.api.entity.Weather;

public interface WeatherValidator {
    void validate(Weather weather) throws ValidationException;
    void setNext(WeatherValidator nextValidator);
}
