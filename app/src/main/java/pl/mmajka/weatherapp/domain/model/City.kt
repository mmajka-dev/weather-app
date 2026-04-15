package pl.mmajka.weatherapp.domain.model

data class City(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val state: String? = null
)