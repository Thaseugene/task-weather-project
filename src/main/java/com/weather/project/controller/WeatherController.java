package com.weather.project.controller;

import com.weather.project.model.WeatherDetails;
import com.weather.project.service.WeatherService;
import com.weather.project.service.exception.DataNotFoundException;
import com.weather.project.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WeatherController {

    public static final String PARAM_NAME_FROM = "from";
    public static final String PARAM_NAME_TO = "to";
    public static final String TEMP_KEY_NAME = "temp_c";
    public static final String WIND_KEY_NAME = "wind_kph";
    public static final String PRESSURE_KEY_NAME = "pressure_mbar";
    public static final String HUMIDITY_KEY_NAME = "humidity";
    public static final String CONDITIONS_KEY_NAME = "weather_conditions";
    public static final String REGION_KEY_NAME = "region";
    public static final String AVERAGE_TEMP_KEY_NAME = "average_temp";

    private static final Logger LOGGER = LogManager.getRootLogger();

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Map<String, Object> sendWeatherInfo() throws ServiceException {
        WeatherDetails weatherDetails = weatherService.takeWeatherDetails();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(TEMP_KEY_NAME, weatherDetails.getTemperature());
        response.put(WIND_KEY_NAME, weatherDetails.getWindVelocity());
        response.put(PRESSURE_KEY_NAME, weatherDetails.getPressure());
        response.put(HUMIDITY_KEY_NAME, weatherDetails.getHumidity());
        response.put(CONDITIONS_KEY_NAME, weatherDetails.getWeatherConditions());
        response.put(REGION_KEY_NAME, weatherDetails.getRegion());

        return response;
    }

    @PostMapping("/average")
    @ResponseBody
    public ResponseEntity<Map<String, Double>> sendAverageTemp(@RequestBody Map<String, String> json) throws
            ServiceException,
            DataNotFoundException,
            HttpMessageNotReadableException {

        String from = json.get(PARAM_NAME_FROM);
        String to = json.get(PARAM_NAME_TO);

        Double averageTemp = BigDecimal.valueOf(weatherService.takeAverageTempData(from, to))
                .setScale(3, RoundingMode.HALF_UP).doubleValue();

        Map<String, Double> response = Collections.singletonMap(AVERAGE_TEMP_KEY_NAME, averageTemp);
        return ResponseEntity.ok(response);

    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException ex) {
        LOGGER.warn("Service exception occurred", ex.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ServiceException occurred: " + ex.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(DataNotFoundException ex) {
        LOGGER.warn("Problems with data is occurred", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadException(HttpMessageNotReadableException ex) {
        LOGGER.warn("Problems with reading request data", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect request format");
    }
}
