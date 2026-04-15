package pl.mmajka.weatherapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.repository.SearchHistoryRepository

class GetSearchHistoryUseCase(
    private val repository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<List<City>> =
        repository.getHistory()
}
