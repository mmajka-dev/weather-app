package pl.mmajka.weatherapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.usecase.GetWeatherUseCase

class WeatherDetailsViewModel(
    private val city: City,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherViewState>(WeatherViewState.Loading)
    val state: StateFlow<WeatherViewState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadWeather()
    }

    fun retry() = loadWeather()

    private fun loadWeather() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = WeatherViewState.Loading
            getWeatherUseCase(city).fold(
                onSuccess = { weather -> _state.value = WeatherViewState.Success(weather) },
                onFailure = { error ->
                    _state.value = WeatherViewState.Error(error.message.orEmpty())
                }
            )
        }
    }
}
