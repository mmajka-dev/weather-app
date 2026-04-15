package pl.mmajka.weatherapp.data.repository.mapper

import pl.mmajka.weatherapp.data.remote.model.CurrentWeatherDto
import pl.mmajka.weatherapp.data.remote.model.ForecastDto
import pl.mmajka.weatherapp.data.remote.model.ForecastEntryDto
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.DailyForecast
import pl.mmajka.weatherapp.domain.model.HourlyForecast
import pl.mmajka.weatherapp.domain.model.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val MAX_DAILY_DAYS = 5
private const val MS_TO_KMH = 3.6
private const val ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png"
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dayFormatter = DateTimeFormatter.ofPattern("EEEE")

fun CurrentWeatherDto.toDomain(city: City, forecast: ForecastDto): Weather = Weather(
    city = city,
    temperature = main.temp,
    feelsLike = main.feelsLike,
    description = weather.firstOrNull()?.description.orEmpty(),
    iconUrl = iconUrl(weather.firstOrNull()?.icon),
    humidity = main.humidity,
    windSpeedKmh = wind.speed * MS_TO_KMH,
    hourlyForecast = forecast.list.map { it.toHourlyForecast() },
    dailyForecast = forecast.toDailyForecast()
)

fun ForecastDto.toDailyForecast(): List<DailyForecast> {
    val zone = ZoneId.systemDefault()
    return list
        .groupBy { entry -> Instant.ofEpochSecond(entry.date).atZone(zone).toLocalDate() }
        .entries
        .sortedBy { it.key }
        .take(MAX_DAILY_DAYS)
        .map { (date, entries) ->
            DailyForecast(
                dayName = date.format(dayFormatter).replaceFirstChar { it.uppercase() },
                minTemperature = entries.minOf { it.main.temp },
                maxTemperature = entries.maxOf { it.main.temp },
                iconUrl = iconUrl(middleEntry(entries).weather.firstOrNull()?.icon)
            )
        }
}

private fun ForecastEntryDto.toHourlyForecast(): HourlyForecast {
    val zonedTime = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault())
    return HourlyForecast(
        formattedTime = zonedTime.format(timeFormatter),
        temperature = main.temp,
        iconUrl = iconUrl(weather.firstOrNull()?.icon)
    )
}

private fun middleEntry(entries: List<ForecastEntryDto>): ForecastEntryDto =
    entries[entries.size / 2]

private fun iconUrl(iconCode: String?): String =
    if (iconCode.isNullOrBlank()) "" else ICON_URL.format(iconCode)
