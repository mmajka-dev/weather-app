package pl.mmajka.weatherapp.data.remote.api

import pl.mmajka.weatherapp.data.remote.model.CityDto
import pl.mmajka.weatherapp.data.remote.model.CurrentWeatherDto
import pl.mmajka.weatherapp.data.remote.model.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("geo/1.0/direct")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): List<CityDto>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = DEFAULT_UNITS
    ): CurrentWeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = DEFAULT_UNITS
    ): ForecastDto

    companion object {
        private const val DEFAULT_UNITS = "metric"
    }
}