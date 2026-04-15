package pl.mmajka.weatherapp.domain.model.error

sealed class SearchQueryError(message: String) : Exception(message) {
    class TooShort : SearchQueryError("Query is too short")
    class InvalidCharacters : SearchQueryError("Query contains invalid characters")
}