package pl.mmajka.weatherapp.ui.navigation

import kotlinx.serialization.Serializable
import pl.mmajka.weatherapp.domain.model.City

@Serializable
data object SearchRoute

@Serializable
data class WeatherDetailsRoute(
    val cityName: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val state: String? = null
)

fun City.toRoute() = WeatherDetailsRoute(
    cityName = name,
    country = country,
    lat = lat,
    lon = lon,
    state = state
)

fun WeatherDetailsRoute.toCity() = City(
    name = cityName,
    country = country,
    lat = lat,
    lon = lon,
    state = state
)
