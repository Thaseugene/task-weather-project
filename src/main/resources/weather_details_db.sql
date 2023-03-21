CREATE SCHEMA IF NOT EXISTS weather_service_db;

USE weather_service_db;

CREATE TABLE `weather_details`
(
    id                 INT auto_increment not null primary key,
    region             VARCHAR(255)       not null,
    local_time         DATETIME           not null,
    weather_conditions VARCHAR(255)       not null,
    temp_c             DOUBLE             not null,
    wind_kph           DOUBLE             not null,
    pressure_mb        DOUBLE             not null,
    humidity           DOUBLE             not null

)
    ENGINE = InnoDB;

-- Test examples of entities

INSERT INTO weather_details (region, local_time, weather_conditions, temp_c, wind_kph, pressure_mb, humidity)
VALUES ('Minsk', '2022-03-21 12:00:00', 'Sunny', 4, 15.4, 1013.5, 60.0);

INSERT INTO weather_details (region, local_time, weather_conditions, temp_c, wind_kph, pressure_mb, humidity)
VALUES ('Minsk', '2022-03-15 13:30:00', 'Sunny', 7.6, 1.2, 1013.5, 65.0);

INSERT INTO weather_details (region, local_time, weather_conditions, temp_c, wind_kph, pressure_mb, humidity)
VALUES ('Minsk', '2022-03-12 11:20:00', 'Sunny', 4.4, 5.2, 1013.5, 70.0);