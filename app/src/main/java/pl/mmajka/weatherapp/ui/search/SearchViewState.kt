package pl.mmajka.weatherapp.ui.search

import androidx.compose.runtime.Stable
import pl.mmajka.weatherapp.domain.model.City

@Stable
data class SearchViewState(
    val query: String = "",
    val history: List<City> = emptyList(),
    val searchState: SearchState = SearchState.Idle
)

sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    @Stable
    data class Cities(val cities: List<City>) : SearchState
    data class Error(val message: String) : SearchState
    data object InvalidQuery : SearchState
}
