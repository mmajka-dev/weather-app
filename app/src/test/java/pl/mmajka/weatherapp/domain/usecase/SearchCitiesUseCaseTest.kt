package pl.mmajka.weatherapp.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.error.SearchQueryError
import pl.mmajka.weatherapp.domain.repository.WeatherRepository

class SearchCitiesUseCaseTest {

    private val repository: WeatherRepository = mockk()
    private lateinit var useCase: SearchCitiesUseCase

    @Before
    fun setUp() {
        useCase = SearchCitiesUseCase(repository)
    }

    @Test
    fun `empty query returns too short error`() = runTest {
        val result = useCase("")

        assertTrue(result.exceptionOrNull() is SearchQueryError.TooShort)
    }

    @Test
    fun `single character query returns too short error`() = runTest {
        val result = useCase("W")

        assertTrue(result.exceptionOrNull() is SearchQueryError.TooShort)
    }

    @Test
    fun `query with digits returns invalid characters error`() = runTest {
        val result = useCase("W4rsaw")

        assertTrue(result.exceptionOrNull() is SearchQueryError.InvalidCharacters)
    }

    @Test
    fun `query with special characters returns invalid characters error`() = runTest {
        val result = useCase("War@saw")

        assertTrue(result.exceptionOrNull() is SearchQueryError.InvalidCharacters)
    }

    @Test
    fun `two character query is valid and returns results`() = runTest {
        coEvery { repository.searchCities("PL") } returns Result.success(emptyList())

        val result = useCase("PL")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `hyphenated city names are accepted`() = runTest {
        coEvery { repository.searchCities("Baden-Baden") } returns Result.success(emptyList())

        val result = useCase("Baden-Baden")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `city names with apostrophes are accepted`() = runTest {
        coEvery { repository.searchCities("L'viv") } returns Result.success(emptyList())

        val result = useCase("L'viv")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `cities from the repository are returned to the caller`() = runTest {
        coEvery { repository.searchCities("Warsaw") } returns Result.success(listOf(warsaw))

        val result = useCase("Warsaw")

        assertEquals(listOf(warsaw), result.getOrThrow())
    }

    @Test
    fun `repository failure is propagated unchanged`() = runTest {
        val error = RuntimeException("Network error")
        coEvery { repository.searchCities("Warsaw") } returns Result.failure(error)

        val result = useCase("Warsaw")

        assertEquals(error, result.exceptionOrNull())
    }

    // --- deduplication ---

    @Test
    fun `cities with the same coordinates are deduplicated`() = runTest {
        val duplicate = warsaw.copy(name = "Warszawa")
        coEvery { repository.searchCities("Warsaw") } returns Result.success(listOf(warsaw, duplicate))

        val result = useCase("Warsaw")

        assertEquals(1, result.getOrThrow().size)
        assertEquals(warsaw, result.getOrThrow().first())
    }

    @Test
    fun `cities with different coordinates are all returned`() = runTest {
        coEvery { repository.searchCities("City") } returns Result.success(listOf(warsaw, krakow))

        val result = useCase("City")

        assertEquals(2, result.getOrThrow().size)
    }

    companion object {
        private val warsaw = City("Warsaw", "PL", 52.23, 21.01)
        private val krakow = City("Krakow", "PL", 50.06, 19.94)
    }
}
