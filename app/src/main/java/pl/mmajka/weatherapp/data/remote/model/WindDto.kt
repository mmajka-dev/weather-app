package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class WindDto(
    val speed: Double
)