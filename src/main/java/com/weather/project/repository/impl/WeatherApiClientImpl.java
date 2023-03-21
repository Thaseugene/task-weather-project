package com.weather.project.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.weather.project.model.weather.api.entity.Weather;
import com.weather.project.repository.WeatherApiClient;
import com.weather.project.repository.exception.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class WeatherApiClientImpl implements WeatherApiClient {

    public static final String API_URL = "https://weatherapi-com.p.rapidapi.com/current.json?q=q%3DBelarus%20Minsk";
    public static final String API_KEY = "X-RapidAPI-Key";
    public static final String API_HOST = "X-RapidAPI-Host";
    public static final String API_HOST_VALUE = "weatherapi-com.p.rapidapi.com";
    public static final String API_KEY_VALUE = "b542a92c6amsha29a19861a34307p1240cfjsn1a8b103a71eb";
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private static final Logger LOGGER = LogManager.getRootLogger();

    @Override
    public Weather getWeather() throws RepositoryException {
        Weather weather;
        try {
            HttpResponse<String> response = Unirest.get(API_URL)
                    .header(API_KEY, API_KEY_VALUE)
                    .header(API_HOST, API_HOST_VALUE)
                    .asString();
            DateFormat df = new SimpleDateFormat(DATE_PATTERN);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(df);

            weather = objectMapper.readValue(response.getBody(), Weather.class);

        } catch (JsonProcessingException e) {
            LOGGER.warn("JSON parsing problems problems are presented", e);
            throw new RepositoryException("JSON parsing problems problems are presented", e);
        } catch (UnirestException e) {
            LOGGER.warn("API connection problems are presented", e);
            throw new RepositoryException("API connection problems are presented", e);
        }
        return weather;
    }

}
