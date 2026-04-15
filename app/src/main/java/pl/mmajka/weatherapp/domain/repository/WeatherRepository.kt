package pl.mmajka.weatherapp.domain.repository

import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.Weather

interface WeatherRepository {
    suspend fun searchCities(query: String): Result<List<City>>
    suspend fun getWeather(city: City): Result<Weather>
}
