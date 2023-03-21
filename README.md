# task-weather-project

Первый endpoint -  GET запрос по ссылке http://localhost:8080/weather

Пример полученных данных:
{
  "temp_c": 5,
  "wind_kph": 6.8,
  "pressure_mbar": 1013,
  "humidity": 87,
  "weather_conditions": "Partly cloudy",
  "region": "Minsk"
}

Второй endpoint -  POST запрос по ссылке http://localhost:8080/average
С телом запроса (как пример):
{
"from": "10-03-2023",
"to": "19-03-2023"
}

Скрипты создания БД находятся ---> src/main/resources/weather_details_db.sql
Конфигурация пути к БД находится -----> src/main/resources/application.properties
