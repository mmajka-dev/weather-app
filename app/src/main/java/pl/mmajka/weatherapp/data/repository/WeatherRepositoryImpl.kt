package pl.mmajka.weatherapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.mmajka.weatherapp.data.remote.api.WeatherApi
import pl.mmajka.weatherapp.data.repository.mapper.toDomain
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.Weather
import pl.mmajka.weatherapp.domain.repository.WeatherRepository
import pl.mmajka.weatherapp.util.suspendRunCatching

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dispatcher: CoroutineDispatcher
) : WeatherRepository {
    override suspend fun searchCities(query: String): Result<List<City>> = withContext(dispatcher) {
        suspendRunCatching {
            api.searchCities(query).map { it.toDomain() }
        }
    }

    override suspend fun getWeather(city: City): Result<Weather> = withContext(dispatcher) {
        suspendRunCatching {
            val weather = api.getCurrentWeather(lat = city.lat, lon = city.lon)
            val forecast = api.getForecast(lat = city.lat, lon = city.lon)
            weather.toDomain(city, forecast)
        }
    }
}