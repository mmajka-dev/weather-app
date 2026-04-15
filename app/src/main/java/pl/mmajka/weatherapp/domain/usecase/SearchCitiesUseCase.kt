package pl.mmajka.weatherapp.domain.usecase

import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.error.SearchQueryError
import pl.mmajka.weatherapp.domain.repository.WeatherRepository

private val QUERY_REGEX = Regex("""^[\p{L}\s\-']+$""")
private const val MIN_QUERY_LENGTH = 2

class SearchCitiesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): Result<List<City>> = when {
        query.length < MIN_QUERY_LENGTH ->
            Result.failure(SearchQueryError.TooShort())
        !QUERY_REGEX.matches(query) ->
            Result.failure(SearchQueryError.InvalidCharacters())
        else -> repository.searchCities(query).map { cities ->
            cities.distinctBy { it.lat to it.lon }
        }
    }
}
