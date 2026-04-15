package pl.mmajka.weatherapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.components.GlassCard
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

internal enum class CityItemVariant { Suggestion, History }

@Composable
internal fun CityListItem(
    city: City,
    variant: CityItemVariant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    val containerSize =
        if (variant == CityItemVariant.History) dimensions.iconContainerL else dimensions.iconContainerM
    val containerRadius =
        if (variant == CityItemVariant.History) dimensions.radiusMedium else dimensions.radiusSmall
    val iconSize = if (variant == CityItemVariant.History) dimensions.iconM else dimensions.iconS
    val nameStyle = if (variant == CityItemVariant.History) {
        MaterialTheme.typography.bodyLarge.copy(
            fontSize = SUBTITLE_FONT_SIZE.sp,
            fontWeight = FontWeight.Medium
        )
    } else {
        MaterialTheme.typography.bodyLarge
    }

    GlassCard(modifier = modifier, onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(containerSize)
                    .background(
                        colors.accent.copy(alpha = 0.1f),
                        RoundedCornerShape(containerRadius)
                    )
                    .then(
                        if (variant == CityItemVariant.History) {
                            Modifier.border(
                                dimensions.borderWidth,
                                colors.accentLight.copy(alpha = 0.2f),
                                RoundedCornerShape(containerRadius)
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = colors.accentLight,
                    modifier = Modifier.size(iconSize)
                )
            }
            Spacer(modifier = Modifier.width(dimensions.spacingL))
            Column {
                Text(text = city.name, style = nameStyle, color = Color.White)
                Text(
                    text = buildCitySubtitle(city),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.muted
                )
            }
        }
    }
}

private fun buildCitySubtitle(city: City): String = buildString {
    append(city.country)
    if (city.state != null) append(" \u00B7 ${city.state}")
}
