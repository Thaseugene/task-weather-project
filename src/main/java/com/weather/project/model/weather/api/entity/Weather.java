package com.weather.project.model.weather.api.entity;

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
public class Weather implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("current")
    private Current current;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return Objects.equals(location, weather.location) && Objects.equals(current, weather.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, current);
    }
}
