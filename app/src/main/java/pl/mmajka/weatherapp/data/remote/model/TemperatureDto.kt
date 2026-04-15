package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TemperatureDto(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int
)