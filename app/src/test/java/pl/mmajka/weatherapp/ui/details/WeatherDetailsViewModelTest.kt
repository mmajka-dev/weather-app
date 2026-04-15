package pl.mmajka.weatherapp.ui.details

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.DailyForecast
import pl.mmajka.weatherapp.domain.model.HourlyForecast
import pl.mmajka.weatherapp.domain.model.Weather
import pl.mmajka.weatherapp.domain.usecase.GetWeatherUseCase
import pl.mmajka.weatherapp.util.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val getWeatherUseCase: GetWeatherUseCase = mockk()

    private fun viewModel() = WeatherDetailsViewModel(city, getWeatherUseCase)

    @Test
    fun `state is loading while weather is being fetched`() = runTest(mainDispatcherRule.dispatcher) {
        val deferred = CompletableDeferred<Result<Weather>>()
        coEvery { getWeatherUseCase(city) } coAnswers { deferred.await() }

        assertTrue(viewModel().state.value is WeatherViewState.Loading)
    }

    @Test
    fun `successful load shows weather in state`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returns Result.success(weather)

        val vm = viewModel()
        advanceUntilIdle()

        val state = vm.state.value as WeatherViewState.Success
        assertEquals(weather, state.weather)
    }

    @Test
    fun `failed load shows the exception message in error state`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returns Result.failure(RuntimeException("Timeout"))

        val vm = viewModel()
        advanceUntilIdle()

        assertEquals("Timeout", (vm.state.value as WeatherViewState.Error).message)
    }

    @Test
    fun `exception without a message results in empty error string`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returns Result.failure(RuntimeException())

        val vm = viewModel()
        advanceUntilIdle()

        assertEquals("", (vm.state.value as WeatherViewState.Error).message)
    }

    @Test
    fun `retry after failure eventually shows weather`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returnsMany listOf(
            Result.failure(RuntimeException("First attempt failed")),
            Result.success(weather)
        )
        val vm = viewModel()
        advanceUntilIdle()

        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.state.value is WeatherViewState.Success)
    }

    @Test
    fun `state is loading while retry is in flight`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returns Result.failure(RuntimeException("error"))
        val vm = viewModel()
        advanceUntilIdle()

        val deferred = CompletableDeferred<Result<Weather>>()
        coEvery { getWeatherUseCase(city) } coAnswers { deferred.await() }
        vm.retry()
        runCurrent()

        assertEquals(WeatherViewState.Loading, vm.state.value)
    }

    @Test
    fun `multiple rapid retries still end with success`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { getWeatherUseCase(city) } returns Result.success(weather)
        val vm = viewModel()

        vm.retry()
        vm.retry()
        advanceUntilIdle()

        assertTrue(vm.state.value is WeatherViewState.Success)
    }

    companion object {
        private val city = City("Warsaw", "PL", 52.23, 21.01)
        private val weather = Weather(
            city = city,
            temperature = 18.0,
            feelsLike = 16.0,
            description = "clear sky",
            iconUrl = "https://example.com/icon.png",
            humidity = 65,
            windSpeedKmh = 12.0,
            hourlyForecast = emptyList<HourlyForecast>(),
            dailyForecast = emptyList<DailyForecast>()
        )
    }
}
