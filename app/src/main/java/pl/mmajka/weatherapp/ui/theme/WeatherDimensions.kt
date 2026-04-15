package pl.mmajka.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WeatherDimensions(
    val radiusSmall: Dp,
    val radiusMedium: Dp,
    val radiusLarge: Dp,
    val radiusPill: Dp,
    val spacingXxs: Dp,
    val spacingXs: Dp,
    val spacingS: Dp,
    val spacingM: Dp,
    val spacingL: Dp,
    val spacingXl: Dp,
    val spacingXxl: Dp,
    val spacingXxxl: Dp,
    val spacingHuge: Dp,
    val iconXs: Dp,
    val iconS: Dp,
    val iconM: Dp,
    val iconL: Dp,
    val iconContainerM: Dp,
    val iconContainerL: Dp,
    val searchFieldHeight: Dp,
    val backButtonSize: Dp,
    val weatherIconSize: Dp,
    val borderWidth: Dp,
    val strokeWidth: Dp,
)

val weatherDimensions = WeatherDimensions(
    radiusSmall = 12.dp,
    radiusMedium = 16.dp,
    radiusLarge = 24.dp,
    radiusPill = 100.dp,
    spacingXxs = 4.dp,
    spacingXs = 6.dp,
    spacingS = 8.dp,
    spacingM = 12.dp,
    spacingL = 16.dp,
    spacingXl = 20.dp,
    spacingXxl = 24.dp,
    spacingXxxl = 32.dp,
    spacingHuge = 40.dp,
    iconXs = 16.dp,
    iconS = 20.dp,
    iconM = 24.dp,
    iconL = 32.dp,
    iconContainerM = 40.dp,
    iconContainerL = 56.dp,
    searchFieldHeight = 64.dp,
    backButtonSize = 52.dp,
    weatherIconSize = 72.dp,
    borderWidth = 1.dp,
    strokeWidth = 1.5.dp,
)

val LocalWeatherDimensions = staticCompositionLocalOf { weatherDimensions }
