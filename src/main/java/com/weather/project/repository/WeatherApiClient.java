package com.weather.project.repository;

import com.weather.project.model.weather.api.entity.Weather;
import com.weather.project.repository.exception.RepositoryException;

public interface WeatherApiClient {

    Weather getWeather() throws RepositoryException;
}
