package pl.mmajka.weatherapp.ui.details

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.domain.model.Weather
import pl.mmajka.weatherapp.ui.components.GlassCard
import pl.mmajka.weatherapp.ui.theme.WeatherTheme
import pl.mmajka.weatherapp.ui.theme.forTemperature

private const val ANIM_DURATION = 400
private const val ANIM_SLIDE_DIVISOR = 10
private const val TEMP_FONT_SIZE = 72

@Composable
internal fun WeatherContent(weather: Weather, onBack: () -> Unit) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(ANIM_DURATION))
                + slideInVertically(tween(ANIM_DURATION)) { it / ANIM_SLIDE_DIVISOR },
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                BackButton(onBack = onBack)
            }
            Spacer(modifier = Modifier.height(dimensions.spacingS))
            Text(
                text = weather.city.name,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(dimensions.spacingXxs))
            Text(
                text = weather.city.country,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.muted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(dimensions.spacingL))
            WeatherIcon(weather = weather)
            TemperatureDisplay(weather = weather)
            Spacer(modifier = Modifier.height(dimensions.spacingXxs))
            Text(
                text = stringResource(R.string.weather_feels_like, weather.feelsLike.toInt()),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.muted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(dimensions.spacingXxl))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacingXxxl)
            ) {
                StatsRow(weather = weather)
                HourlySection(forecast = weather.hourlyForecast.take(FORECAST_HOURS))
                DailySection(forecast = weather.dailyForecast)
            }
            Spacer(modifier = Modifier.height(dimensions.spacingHuge))
        }
    }
}

@Composable
private fun BackButton(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val dimensions = WeatherTheme.dimensions

    Box(
        modifier = modifier
            .padding(start = dimensions.spacingL, top = dimensions.spacingS)
            .size(dimensions.backButtonSize)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(dimensions.radiusMedium)
            )
            .border(
                dimensions.borderWidth,
                Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(dimensions.radiusMedium)
            )
            .clickable(onClick = onBack),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button_description),
            tint = Color.White,
            modifier = Modifier.size(dimensions.iconM)
        )
    }
}

@Composable
private fun WeatherIcon(weather: Weather) {
    val dimensions = WeatherTheme.dimensions
    AsyncImage(
        model = weather.iconUrl,
        contentDescription = weather.description,
        modifier = Modifier.size(dimensions.weatherIconSize),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun TemperatureDisplay(weather: Weather) {
    val colors = WeatherTheme.colors

    Text(
        text = "${weather.temperature.toInt()}\u00B0",
        fontSize = TEMP_FONT_SIZE.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        color = colors.forTemperature(weather.temperature),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun StatsRow(weather: Weather) {
    val dimensions = WeatherTheme.dimensions
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spacingXxl),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacingM)
    ) {
        StatCard(
            labelRes = R.string.weather_stat_wind,
            value = stringResource(R.string.weather_wind_speed, weather.windSpeedKmh.toInt()),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            labelRes = R.string.weather_stat_humidity,
            value = stringResource(R.string.weather_humidity_value, weather.humidity),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    @StringRes labelRes: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingXs),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall,
                color = colors.muted
            )
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}
