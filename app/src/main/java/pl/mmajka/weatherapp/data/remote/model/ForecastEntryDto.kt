package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastEntryDto(
    @SerialName("dt")
    val date: Long,
    val main: TemperatureDto,
    val weather: List<WeatherConditionDto>
)