package pl.mmajka.weatherapp.domain.usecase

import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.repository.SearchHistoryRepository

class SaveSearchHistoryUseCase(
    private val repository: SearchHistoryRepository
) {
    suspend operator fun invoke(city: City) =
        repository.saveCity(city)
}
