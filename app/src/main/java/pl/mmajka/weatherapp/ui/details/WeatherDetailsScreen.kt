package pl.mmajka.weatherapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.components.WeatherBackground
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

internal const val FORECAST_HOURS = 8

@Composable
fun WeatherDetailsScreen(
    city: City,
    onBack: () -> Unit,
    viewModel: WeatherDetailsViewModel = koinViewModel(parameters = { parametersOf(city) })
) {
    val state by viewModel.state.collectAsState()
    val colors = WeatherTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
    ) {
        WeatherBackground()

        when (val currentState = state) {
            WeatherViewState.Loading -> LoadingContent()
            is WeatherViewState.Success -> WeatherContent(
                weather = currentState.weather,
                onBack = onBack
            )
            is WeatherViewState.Error -> ErrorContent(
                message = currentState.message.ifEmpty { stringResource(R.string.error_unknown) },
                onRetry = viewModel::retry
            )
        }
    }
}
