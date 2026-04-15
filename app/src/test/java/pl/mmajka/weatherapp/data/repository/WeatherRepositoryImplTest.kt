package pl.mmajka.weatherapp.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.mmajka.weatherapp.data.remote.api.WeatherApi
import pl.mmajka.weatherapp.data.remote.model.CityDto
import pl.mmajka.weatherapp.data.remote.model.CountryInfoDto
import pl.mmajka.weatherapp.data.remote.model.CurrentWeatherDto
import pl.mmajka.weatherapp.data.remote.model.ForecastDto
import pl.mmajka.weatherapp.data.remote.model.TemperatureDto
import pl.mmajka.weatherapp.data.remote.model.WeatherConditionDto
import pl.mmajka.weatherapp.data.remote.model.WindDto
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.util.MainDispatcherRule

class WeatherRepositoryImplTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val api: WeatherApi = mockk()
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(api, dispatcherRule.dispatcher)
    }

    @Test
    fun `city dtos from the api are mapped to domain cities`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.searchCities("Warsaw") } returns listOf(cityDto)

        val result = repository.searchCities("Warsaw")

        assertEquals(listOf(city), result.getOrThrow())
    }

    @Test
    fun `api error during city search is wrapped in failure`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.searchCities("Warsaw") } throws RuntimeException("HTTP 401")

        val result = repository.searchCities("Warsaw")

        assertTrue(result.isFailure)
        assertEquals("HTTP 401", result.exceptionOrNull()?.message)
    }

    @Test
    fun `api returning no cities produces an empty list`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.searchCities("Unknown") } returns emptyList()

        val result = repository.searchCities("Unknown")

        assertEquals(emptyList<City>(), result.getOrThrow())
    }

    @Test
    fun `state field in city dto is mapped to the domain model`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.searchCities("Warsaw") } returns listOf(cityDto.copy(state = "Masovian"))

        val result = repository.searchCities("Warsaw")

        assertEquals("Masovian", result.getOrThrow().first().state)
    }

    // --- getWeather ---

    @Test
    fun `fetching weather calls both the current weather and forecast endpoints`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.getCurrentWeather(city.lat, city.lon) } returns currentWeatherDto
        coEvery { api.getForecast(city.lat, city.lon) } returns forecastDto

        repository.getWeather(city)

        coVerify(exactly = 1) { api.getCurrentWeather(city.lat, city.lon) }
        coVerify(exactly = 1) { api.getForecast(city.lat, city.lon) }
    }

    @Test
    fun `temperature fields from the dto are mapped to the domain model`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.getCurrentWeather(city.lat, city.lon) } returns currentWeatherDto
        coEvery { api.getForecast(city.lat, city.lon) } returns forecastDto

        val weather = repository.getWeather(city).getOrThrow()

        assertEquals(18.0, weather.temperature, 0.001)
        assertEquals(16.0, weather.feelsLike, 0.001)
        assertEquals(65, weather.humidity)
    }

    @Test
    fun `wind speed is converted from m per s to km per h`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.getCurrentWeather(city.lat, city.lon) } returns currentWeatherDto
        coEvery { api.getForecast(city.lat, city.lon) } returns forecastDto

        val weather = repository.getWeather(city).getOrThrow()

        assertEquals(3.5 * 3.6, weather.windSpeedKmh, 0.01)
    }

    @Test
    fun `current weather endpoint failure is wrapped in failure result`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.getCurrentWeather(city.lat, city.lon) } throws RuntimeException("Timeout")
        coEvery { api.getForecast(city.lat, city.lon) } returns forecastDto

        val result = repository.getWeather(city)

        assertTrue(result.isFailure)
        assertEquals("Timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `forecast endpoint failure is wrapped in failure result`() = runTest(dispatcherRule.dispatcher) {
        coEvery { api.getCurrentWeather(city.lat, city.lon) } returns currentWeatherDto
        coEvery { api.getForecast(city.lat, city.lon) } throws RuntimeException("Server error")

        assertTrue(repository.getWeather(city).isFailure)
    }

    companion object {
        private val city = City("Warsaw", "PL", 52.23, 21.01)
        private val cityDto = CityDto("Warsaw", "PL", 52.23, 21.01)
        private val currentWeatherDto = CurrentWeatherDto(
            name = "Warsaw",
            main = TemperatureDto(temp = 18.0, feelsLike = 16.0, humidity = 65),
            weather = listOf(WeatherConditionDto(description = "clear sky", icon = "01d")),
            wind = WindDto(speed = 3.5),
            sys = CountryInfoDto(country = "PL")
        )
        private val forecastDto = ForecastDto(list = emptyList())
    }
}
