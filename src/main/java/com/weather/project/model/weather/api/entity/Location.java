package com.weather.project.model.weather.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("region")
    private String region;

    @JsonProperty("localtime")
    private Date localTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(region, location.region) && Objects.equals(localTime, location.localTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, localTime);
    }
}
