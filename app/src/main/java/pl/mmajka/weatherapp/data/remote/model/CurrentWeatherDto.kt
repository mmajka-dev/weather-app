package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    val name: String,
    val main: TemperatureDto,
    val weather: List<WeatherConditionDto>,
    val wind: WindDto,
    val sys: CountryInfoDto,
    val visibility: Int? = null
)