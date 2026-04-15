package pl.mmajka.weatherapp.ui.search

import pl.mmajka.weatherapp.domain.model.City

sealed interface NavEvent {
    data class NavigateToDetails(val city: City) : NavEvent
}
