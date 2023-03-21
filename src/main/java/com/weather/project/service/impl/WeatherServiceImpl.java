package com.weather.project.service.impl;

import com.weather.project.model.WeatherDetails;
import com.weather.project.model.weather.api.entity.Weather;
import com.weather.project.repository.WeatherApiClient;
import com.weather.project.repository.WeatherRepository;
import com.weather.project.repository.exception.RepositoryException;
import com.weather.project.service.WeatherService;
import com.weather.project.service.exception.DataNotFoundException;
import com.weather.project.service.exception.ServiceException;
import com.weather.project.util.validation.ContentChecker;
import com.weather.project.util.validation.weather.ValidationException;
import com.weather.project.util.validation.weather.WeatherValidatorChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@EnableScheduling
public class WeatherServiceImpl implements WeatherService {

    public static final String DATE_PATTERN = "dd-MM-yyyy";

    private static final Logger LOGGER = LogManager.getRootLogger();
    private final WeatherApiClient apiClient;
    private final WeatherRepository repository;
    private final WeatherValidatorChain validator;

    private final ContentChecker contentChecker;

    @Autowired
    public WeatherServiceImpl(WeatherApiClient apiClient, WeatherRepository repository, WeatherValidatorChain validator, ContentChecker contentChecker) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.validator = validator;
        this.contentChecker = contentChecker;
    }

    //method takes weather details values
    @Override
    public WeatherDetails takeWeatherDetails() throws ServiceException {
        try {
            return repository.takeLastWeatherResult();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    //save to DB every 5min
    @Override
    @Scheduled(fixedDelay = 300000)
    public void saveWeatherDetails() throws ServiceException {
        try {
            Weather weather = apiClient.getWeather();
            validator.validate(weather);
            repository.saveWeatherData(new WeatherDetails(
                    weather.getLocation().getRegion(),
                    weather.getLocation().getLocalTime(),
                    weather.getCurrent().getCondition().getText(),
                    weather.getCurrent().getTemp(),
                    weather.getCurrent().getWindVelocity(),
                    weather.getCurrent().getPressure(),
                    weather.getCurrent().getHumidity()
            ));
        } catch (ValidationException e) {
            throw new ServiceException("Weather data is invalid: " + e.getMessage());
        } catch (RepositoryException e) {
            throw new ServiceException(e);

        }
    }

    //method returns average temperature value
    @Override
    public Double takeAverageTempData(String dateFrom, String dateTo) throws ServiceException, DataNotFoundException {
        try {

            if (contentChecker.isEmpty(dateFrom, dateTo)) {
                throw new ServiceException("Incorrect date format");
            } else {

                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
                LocalDate date = LocalDate.parse(dateTo, formatter);
                LocalDateTime localDateTime = date.atTime(LocalTime.MAX);
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                Date from = dateFormat.parse(dateFrom);
                Date to = Date.from(instant);

                return (repository.takeWeatherDetailsByRange(from, to)
                        .stream()
                        .mapToDouble(WeatherDetails::getTemperature)
                        .average()
                        .orElseThrow(() -> new DataNotFoundException("No average temperature data found")));
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        } catch (ParseException e) {
            LOGGER.warn("Problems with parsing dates", e);
            throw new ServiceException("Incorrect date format");
        }
    }

}
