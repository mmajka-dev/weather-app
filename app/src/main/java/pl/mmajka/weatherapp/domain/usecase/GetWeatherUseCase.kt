package pl.mmajka.weatherapp.domain.usecase

import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.Weather
import pl.mmajka.weatherapp.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City): Result<Weather> =
        repository.getWeather(city)
}