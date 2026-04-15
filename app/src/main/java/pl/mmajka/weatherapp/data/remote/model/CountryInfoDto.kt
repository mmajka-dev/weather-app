package pl.mmajka.weatherapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CountryInfoDto(
    val country: String
)