package pl.mmajka.weatherapp.ui.search

import pl.mmajka.weatherapp.domain.model.City

sealed interface UiAction {
    data class QueryChanged(val query: String) : UiAction
    data class CitySelected(val city: City) : UiAction
    data class HistoryEntrySelected(val city: City) : UiAction
    data object ClearQuery : UiAction
}
