package com.weather.project.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "weather_details")
public class WeatherDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "region")
    private String region;
    @Column(name = "local_time")
    private Date localTime;
    @Column(name = "weather_conditions")
    private String weatherConditions;
    @Column(name = "temp_c")
    private double temperature;
    @Column(name = "wind_kph")
    private double windVelocity;
    @Column(name = "pressure_mb")
    private double pressure;
    @Column(name = "humidity")
    private double humidity;

    public WeatherDetails(String region, Date localTime, String weatherConditions, double temperature, double windVelocity, double pressure, double humidity) {
        this.region = region;
        this.localTime = localTime;
        this.weatherConditions = weatherConditions;
        this.temperature = temperature;
        this.windVelocity = windVelocity;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherDetails that = (WeatherDetails) o;
        return id == that.id && Double.compare(that.temperature, temperature) == 0 && Double.compare(that.windVelocity, windVelocity) == 0 && Double.compare(that.pressure, pressure) == 0 && Double.compare(that.humidity, humidity) == 0 && Objects.equals(region, that.region) && Objects.equals(localTime, that.localTime) && Objects.equals(weatherConditions, that.weatherConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, region, localTime, weatherConditions, temperature, windVelocity, pressure, humidity);
    }
}
