package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherConditionDto(
    val description: String,
    val icon: String
)