package pl.mmajka.weatherapp.domain.model

data class DailyForecast(
    val dayName: String,
    val minTemperature: Double,
    val maxTemperature: Double,
    val iconUrl: String
)
