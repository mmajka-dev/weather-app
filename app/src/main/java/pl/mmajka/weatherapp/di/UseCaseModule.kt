package pl.mmajka.weatherapp.di

import org.koin.dsl.module
import pl.mmajka.weatherapp.domain.usecase.GetSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.GetWeatherUseCase
import pl.mmajka.weatherapp.domain.usecase.SaveSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.SearchCitiesUseCase

val useCaseModule = module {
    factory { GetWeatherUseCase(get()) }
    factory { SearchCitiesUseCase(get()) }
    factory { GetSearchHistoryUseCase(get()) }
    factory { SaveSearchHistoryUseCase(get()) }
}
