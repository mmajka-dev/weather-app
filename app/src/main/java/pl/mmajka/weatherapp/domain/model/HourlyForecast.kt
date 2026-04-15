package pl.mmajka.weatherapp.domain.model

data class HourlyForecast(
    val formattedTime: String,
    val temperature: Double,
    val iconUrl: String
)
