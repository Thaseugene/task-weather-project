package com.weather.project.util.validation.weather;

import com.weather.project.model.weather.api.entity.Weather;
import org.springframework.stereotype.Component;

@Component
public class WeatherValidatorChain {
    private final WeatherValidator firstValidator;

    public WeatherValidatorChain() {
        firstValidator = new LocationValidator();
        WeatherValidator secondValidator = new CurrentValidator();
        firstValidator.setNext(secondValidator);
    }

    public void validate(Weather weather) throws ValidationException {
        firstValidator.validate(weather);
    }
}
