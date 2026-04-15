package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val state: String? = null
)