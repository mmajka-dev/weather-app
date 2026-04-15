package pl.mmajka.weatherapp.domain.model

data class Weather(
    val city: City,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val iconUrl: String,
    val humidity: Int,
    val windSpeedKmh: Double,
    val hourlyForecast: List<HourlyForecast>,
    val dailyForecast: List<DailyForecast>
)
