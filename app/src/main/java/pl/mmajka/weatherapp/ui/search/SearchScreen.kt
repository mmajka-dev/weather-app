package pl.mmajka.weatherapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.components.WeatherBackground
import pl.mmajka.weatherapp.ui.theme.WeatherTheme

internal const val TITLE_DELAY = 300L
internal const val HISTORY_BASE_DELAY = 500L
internal const val HISTORY_ITEM_DELAY = 100L
internal const val SUGGESTION_ITEM_DELAY = 50L
internal const val TITLE_FONT_SIZE = 56
internal const val SUBTITLE_FONT_SIZE = 18

@Composable
fun SearchScreen(
    onNavigateToDetails: (City) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val colors = WeatherTheme.colors
    val dimensions = WeatherTheme.dimensions

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is NavEvent.NavigateToDetails -> onNavigateToDetails(event.city)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        WeatherBackground()
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedTitle(
                modifier = Modifier.padding(
                    horizontal = dimensions.spacingXxl,
                    vertical = dimensions.spacingXxxl
                )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.spacingXxl)
            ) {
                GlassSearchField(
                    value = state.query,
                    onValueChange = { viewModel.onAction(UiAction.QueryChanged(it)) }
                )
                SearchResultsList(
                    searchState = state.searchState,
                    onCityClick = { viewModel.onAction(UiAction.CitySelected(it)) }
                )
            }
            Spacer(modifier = Modifier.height(dimensions.spacingXxxl))
            HistorySection(
                entries = state.history,
                onEntryClick = { city -> viewModel.onAction(UiAction.HistoryEntrySelected(city)) }
            )
        }
    }
}

@Composable
internal fun rememberDelayedVisibility(delayMs: Long): Boolean {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMs)
        visible = true
    }
    return visible
}
