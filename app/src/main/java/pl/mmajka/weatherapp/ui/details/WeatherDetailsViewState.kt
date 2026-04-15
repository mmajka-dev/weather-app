package pl.mmajka.weatherapp.ui.details

import androidx.compose.runtime.Stable
import pl.mmajka.weatherapp.domain.model.Weather

sealed interface WeatherViewState {
    data object Loading : WeatherViewState
    @Stable
    data class Success(val weather: Weather) : WeatherViewState
    data class Error(val message: String) : WeatherViewState
}
