package pl.mmajka.weatherapp.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import pl.mmajka.weatherapp.R
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

@Composable
internal fun SearchResultsList(
    searchState: SearchState,
    onCityClick: (City) -> Unit
) {
    val dimensions = WeatherTheme.dimensions
    when (searchState) {
        SearchState.Loading -> SearchLoadingContent()
        is SearchState.Error -> SearchFeedbackContent(
            title = stringResource(R.string.search_error_title),
            subtitle = searchState.message.ifEmpty { stringResource(R.string.error_unknown) }
        )

        SearchState.InvalidQuery -> SearchFeedbackContent(
            title = stringResource(R.string.search_invalid_query_title),
            subtitle = stringResource(R.string.search_invalid_query_subtitle)
        )

        is SearchState.Cities -> {
            Column(
                modifier = Modifier.padding(top = dimensions.spacingM),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacingS)
            ) {
                searchState.cities.forEachIndexed { index, city ->
                    CityResultItem(
                        city = city,
                        index = index,
                        onClick = { onCityClick(city) }
                    )
                }
            }
        }

        SearchState.Idle -> {}
    }
}

@Composable
private fun CityResultItem(city: City, index: Int, onClick: () -> Unit) {
    val visible = rememberDelayedVisibility(index * SUGGESTION_ITEM_DELAY)
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + expandVertically(tween(200))
    ) {
        CityListItem(
            city = city,
            variant = CityItemVariant.Suggestion,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SearchLoadingContent() {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensions.spacingXxxl),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensions.iconM),
            strokeWidth = dimensions.strokeWidth,
            color = colors.accentLight
        )
    }
}

@Composable
private fun SearchFeedbackContent(title: String, subtitle: String) {
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensions.spacingXxxl),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingXs)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.muted,
                textAlign = TextAlign.Center
            )
        }
    }
}
