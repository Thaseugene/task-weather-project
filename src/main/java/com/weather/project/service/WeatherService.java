package com.weather.project.service;

import com.weather.project.model.WeatherDetails;
import com.weather.project.service.exception.DataNotFoundException;
import com.weather.project.service.exception.ServiceException;

public interface WeatherService {

    WeatherDetails takeWeatherDetails() throws ServiceException;
    void saveWeatherDetails() throws ServiceException;
    Double takeAverageTempData(String dateFrom, String dateTo) throws ServiceException, DataNotFoundException;

}
