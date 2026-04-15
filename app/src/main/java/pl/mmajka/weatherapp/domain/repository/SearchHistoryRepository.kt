package pl.mmajka.weatherapp.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.mmajka.weatherapp.domain.model.City

interface SearchHistoryRepository {
    fun getHistory(): Flow<List<City>>
    suspend fun saveCity(city: City)
}
