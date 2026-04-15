package pl.mmajka.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private fun WeatherColors.toMaterial3ColorScheme() = darkColorScheme(
    primary = accent,
    onPrimary = Color.White,
    primaryContainer = accent.copy(alpha = 0.2f),
    onPrimaryContainer = accentLight,
    secondary = accentLight,
    onSecondary = Color.White,
    secondaryContainer = surface,
    onSecondaryContainer = Color.White,
    background = background,
    onBackground = Color.White,
    surface = surface,
    onSurface = Color.White,
    surfaceVariant = surface,
    onSurfaceVariant = muted,
    outline = muted.copy(alpha = 0.3f),
    outlineVariant = surface,
)

@Composable
fun WeatherTheme(
    content: @Composable () -> Unit,
) {
    val colors = weatherDarkColors

    CompositionLocalProvider(
        LocalWeatherColors provides colors,
        LocalWeatherDimensions provides weatherDimensions,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterial3ColorScheme(),
            typography = weatherTypography,
            content = content,
        )
    }
}

object WeatherTheme {
    val colors: WeatherColors
        @Composable get() = LocalWeatherColors.current

    val dimensions: WeatherDimensions
        @Composable get() = LocalWeatherDimensions.current
}
