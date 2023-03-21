package com.weather.project.repository;

import com.weather.project.model.WeatherDetails;
import com.weather.project.repository.exception.RepositoryException;

import java.util.Date;
import java.util.List;

public interface WeatherRepository {

    void saveWeatherData(WeatherDetails weatherDetails) throws RepositoryException;
    List<WeatherDetails> takeWeatherDetailsByRange(Date dateFrom, Date dateTo) throws RepositoryException;
    WeatherDetails takeLastWeatherResult() throws RepositoryException;

}
