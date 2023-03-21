package com.weather.project.model.weather.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current implements Serializable {

    private static final long serialVersionUID = 1L;

    private Condition condition;

    @JsonProperty("temp_c")
    private double temp;

    @JsonProperty("wind_kph")
    private double windVelocity;

    @JsonProperty("pressure_mb")
    private double pressure;

    @JsonProperty("humidity")
    private double humidity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Current current = (Current) o;
        return Double.compare(current.temp, temp) == 0 && Double.compare(current.windVelocity, windVelocity) == 0 && Double.compare(current.pressure, pressure) == 0 && Double.compare(current.humidity, humidity) == 0 && Objects.equals(condition, current.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, temp, windVelocity, pressure, humidity);
    }
}
