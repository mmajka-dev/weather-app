package pl.mmajka.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class WeatherColors(
    val background: Color,
    val surface: Color,
    val accent: Color,
    val accentLight: Color,
    val cyan: Color,
    val muted: Color,
    val placeholder: Color,
    val sectionLabel: Color,
    val badgeText: Color,
    val tempCold: Color,
    val tempMild: Color,
    val tempWarm: Color,
)

fun WeatherColors.forTemperature(temp: Double): Color = when {
    temp < 10.0 -> tempCold
    temp > 20.0 -> tempWarm
    else -> tempMild
}

val weatherDarkColors = WeatherColors(
    background = Color(0xFF0A1628),
    surface = Color(0xFF0F1D31),
    accent = Color(0xFF3B82F6),
    accentLight = Color(0xFF60A5FA),
    cyan = Color(0xFF06B6D4),
    muted = Color(0xFF94A3B8),
    placeholder = Color(0xFF64748B),
    sectionLabel = Color(0xFFCBD5E1),
    badgeText = Color(0xFF93C5FD),
    tempCold = Color(0xFF60A5FA),
    tempMild = Color.White,
    tempWarm = Color(0xFFF87171),
)

val LocalWeatherColors = staticCompositionLocalOf { weatherDarkColors }
