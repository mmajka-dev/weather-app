package pl.mmajka.weatherapp.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.domain.model.DailyForecast
import pl.mmajka.weatherapp.domain.model.HourlyForecast
import pl.mmajka.weatherapp.ui.components.GlassCard
import pl.mmajka.weatherapp.ui.theme.WeatherTheme
import pl.mmajka.weatherapp.ui.theme.forTemperature

@Composable
internal fun HourlySection(forecast: List<HourlyForecast>) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions
    if (forecast.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(
            text = stringResource(R.string.weather_section_hourly),
            style = MaterialTheme.typography.labelSmall,
            color = colors.sectionLabel,
            modifier = Modifier.padding(start = dimensions.spacingXxl, bottom = dimensions.spacingL)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = dimensions.spacingXl),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacingS)
        ) {
            items(forecast, key = { it.formattedTime }) { entry ->
                ForecastCard(entry = entry)
            }
        }
    }
}

@Composable
private fun ForecastCard(entry: HourlyForecast) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    GlassCard {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingXs)
        ) {
            Text(
                text = entry.formattedTime,
                style = MaterialTheme.typography.labelSmall,
                color = colors.muted
            )
            AsyncImage(
                model = entry.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(dimensions.iconL),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "${entry.temperature.toInt()}\u00B0",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = colors.forTemperature(entry.temperature)
            )
        }
    }
}

@Composable
internal fun DailySection(forecast: List<DailyForecast>) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions
    if (forecast.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text(
            text = stringResource(R.string.weather_section_daily),
            style = MaterialTheme.typography.labelSmall,
            color = colors.sectionLabel,
            modifier = Modifier.padding(start = dimensions.spacingXxl, bottom = dimensions.spacingL)
        )
        Column(
            modifier = Modifier.padding(horizontal = dimensions.spacingXl),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingS)
        ) {
            forecast.forEach { day -> DailyRow(day = day) }
        }
    }
}

@Composable
private fun DailyRow(day: DailyForecast) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    GlassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day.dayName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            AsyncImage(
                model = day.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(dimensions.iconL),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(dimensions.spacingM))
            Text(
                text = "${day.minTemperature.toInt()}\u00B0",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.forTemperature(day.minTemperature)
            )
            Spacer(modifier = Modifier.width(dimensions.spacingS))
            Text(
                text = "${day.maxTemperature.toInt()}\u00B0",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = colors.forTemperature(day.maxTemperature)
            )
        }
    }
}
