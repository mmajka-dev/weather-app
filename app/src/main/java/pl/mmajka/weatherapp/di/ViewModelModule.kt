package pl.mmajka.weatherapp.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.ui.details.WeatherDetailsViewModel
import pl.mmajka.weatherapp.ui.search.SearchViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModel { (city: City) -> WeatherDetailsViewModel(city, get()) }
}
