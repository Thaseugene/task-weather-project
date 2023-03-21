package com.weather.project.service.impl;

import com.weather.project.model.WeatherDetails;
import com.weather.project.model.weather.api.entity.Condition;
import com.weather.project.model.weather.api.entity.Current;
import com.weather.project.model.weather.api.entity.Location;
import com.weather.project.model.weather.api.entity.Weather;
import com.weather.project.repository.WeatherApiClient;
import com.weather.project.repository.WeatherRepository;
import com.weather.project.repository.exception.RepositoryException;
import com.weather.project.service.WeatherService;
import com.weather.project.service.exception.DataNotFoundException;
import com.weather.project.service.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplTest {

    public WeatherServiceImplTest() {
    }

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private WeatherRepository weatherRepository;

    @MockBean
    private WeatherApiClient weatherApiClient;

    @Test
    public void testTakeWeatherDetails() throws Exception {
        WeatherDetails expectedDetails = new WeatherDetails("Region", new Date(), "Sunny", 20.0, 10.0, 1000.0, 50.0);

        when(weatherRepository.takeLastWeatherResult()).thenReturn(expectedDetails);
        WeatherDetails actualDetails = weatherService.takeWeatherDetails();
        assertEquals(expectedDetails, actualDetails);

    }

    @Test(expected = ServiceException.class)
    public void testTakeWeatherDetailsFail() throws Exception {

        when(weatherRepository.takeLastWeatherResult()).thenThrow(new RepositoryException());
        weatherService.takeWeatherDetails();

    }

    @Test
    public void testSaveWeatherDetails() throws Exception {

        Weather weather = new Weather(new Location("Region", new Date()), new Current(new Condition("Sunny"), 20.0, 10.0, 1000.0, 50.0));
        when(weatherApiClient.getWeather()).thenReturn(weather);
        weatherService.saveWeatherDetails();
        verify(weatherRepository, times(1)).saveWeatherData(any(WeatherDetails.class));

    }

    @Test(expected = DataNotFoundException.class)
    public void testTakeAverageTempDataFail() throws Exception {

        when(weatherRepository.takeWeatherDetailsByRange(any(Date.class), any(Date.class))).thenReturn(Collections.emptyList());
        weatherService.takeAverageTempData("01-01-2022", "31-01-2022");

    }

    @Test(expected = ServiceException.class)
    public void testTakeAverageTempDataDateFormatFail() throws Exception {
        weatherService.takeAverageTempData("01-2022", "31-01-2022");
    }

    @Test
    public void testTakeAverageTempDataValidData() throws Exception {

        // Create test data
        WeatherDetails weatherTestOne = new WeatherDetails("Region1", new Date(), "Sunny", 20.0, 10.0, 1000.0, 50.0);
        WeatherDetails weatherTestTwo = new WeatherDetails("Region1", new Date(), "Sunny", 25.0, 5.0, 1000.0, 50.0);
        List<WeatherDetails> weatherDetailsList = Arrays.asList(weatherTestOne, weatherTestTwo);

        // Configure mock objects
        when(weatherRepository.takeWeatherDetailsByRange(any(Date.class), any(Date.class))).thenReturn(weatherDetailsList);

        // Call the method under test
        Double result = weatherService.takeAverageTempData("01-01-2022", "31-01-2022");

        // Verify the results
        assertEquals(22.5, result, 0.1);
        verify(weatherRepository, times(1)).takeWeatherDetailsByRange(any(Date.class), any(Date.class));
    }

}