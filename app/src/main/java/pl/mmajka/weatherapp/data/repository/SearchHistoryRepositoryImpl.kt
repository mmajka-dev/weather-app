package pl.mmajka.weatherapp.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.mmajka.weatherapp.data.local.dao.CityDao
import pl.mmajka.weatherapp.data.repository.mapper.toDomain
import pl.mmajka.weatherapp.data.repository.mapper.toEntity
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.repository.SearchHistoryRepository

private const val MAX_HISTORY_SIZE = 10

class SearchHistoryRepositoryImpl(
    private val cityDao: CityDao,
    private val dispatcher: CoroutineDispatcher
) : SearchHistoryRepository {

    override fun getHistory(): Flow<List<City>> =
        cityDao.getHistory(MAX_HISTORY_SIZE).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun saveCity(city: City) = withContext(dispatcher) {
        cityDao.upsertAndTrim(city.toEntity(), MAX_HISTORY_SIZE)
    }
}
