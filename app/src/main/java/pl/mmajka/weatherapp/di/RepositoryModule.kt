package pl.mmajka.weatherapp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import pl.mmajka.weatherapp.data.repository.SearchHistoryRepositoryImpl
import pl.mmajka.weatherapp.data.repository.WeatherRepositoryImpl
import pl.mmajka.weatherapp.domain.repository.SearchHistoryRepository
import pl.mmajka.weatherapp.domain.repository.WeatherRepository


val repositoryModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }
}
