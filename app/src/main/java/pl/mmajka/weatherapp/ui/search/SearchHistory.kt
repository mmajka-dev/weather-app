package pl.mmajka.weatherapp.ui.search

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

@Composable
internal fun HistorySection(
    entries: List<City>,
    onEntryClick: (City) -> Unit
) {
    if (entries.isEmpty()) return
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = dimensions.spacingXxl)
    ) {
        Text(
            text = stringResource(R.string.search_recent_label),
            color = colors.sectionLabel,
            fontSize = SUBTITLE_FONT_SIZE.sp,
            modifier = Modifier.padding(bottom = dimensions.spacingXl)
        )
        Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacingM)) {
            entries.forEachIndexed { index, city ->
                HistoryItem(
                    city = city,
                    index = index,
                    onClick = { onEntryClick(city) }
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(city: City, index: Int, onClick: () -> Unit) {
    val visible = rememberDelayedVisibility(HISTORY_BASE_DELAY + index * HISTORY_ITEM_DELAY)
    val offsetX by animateFloatAsState(
        targetValue = if (visible) 0f else -40f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "historyOffset"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "historyAlpha"
    )
    CityListItem(
        city = city,
        variant = CityItemVariant.History,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX.dp)
            .alpha(alpha)
    )
}
