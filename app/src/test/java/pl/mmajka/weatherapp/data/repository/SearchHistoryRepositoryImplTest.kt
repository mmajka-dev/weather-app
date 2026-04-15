package pl.mmajka.weatherapp.data.repository

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.mmajka.weatherapp.data.local.dao.CityDao
import pl.mmajka.weatherapp.data.local.model.CityEntity
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.util.MainDispatcherRule

class SearchHistoryRepositoryImplTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val dao: CityDao = mockk(relaxed = true)
    private lateinit var repository: SearchHistoryRepositoryImpl

    @Before
    fun setUp() {
        repository = SearchHistoryRepositoryImpl(dao, dispatcherRule.dispatcher)
    }

    @Test
    fun `history entities from dao are mapped to domain cities`() = runTest(dispatcherRule.dispatcher) {
        every { dao.getHistory(10) } returns flowOf(listOf(warsawEntity))

        repository.getHistory().test {
            assertEquals(listOf(warsawCity), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `empty dao result produces empty history`() = runTest(dispatcherRule.dispatcher) {
        every { dao.getHistory(10) } returns flowOf(emptyList())

        repository.getHistory().test {
            assertEquals(emptyList<City>(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `dao is queried with a limit of 10`() = runTest(dispatcherRule.dispatcher) {
        every { dao.getHistory(10) } returns flowOf(emptyList())

        repository.getHistory().test { awaitItem(); awaitComplete() }

        verify { dao.getHistory(10) }
    }

    @Test
    fun `order of cities matches the order returned by dao`() = runTest(dispatcherRule.dispatcher) {
        val krakow = CityEntity(2, "Krakow", "PL", null, 50.06, 19.94, 999_999L)
        every { dao.getHistory(10) } returns flowOf(listOf(warsawEntity, krakow))

        repository.getHistory().test {
            val cities = awaitItem()

            assertEquals("Warsaw", cities[0].name)
            assertEquals("Krakow", cities[1].name)
            awaitComplete()
        }
    }

    @Test
    fun `saving a city upserts the correct entity fields`() = runTest(dispatcherRule.dispatcher) {
        repository.saveCity(warsawCity)

        coVerify {
            dao.upsertAndTrim(
                match { it.name == "Warsaw" && it.country == "PL" && it.lat == 52.23 && it.lon == 21.01 && it.state == null },
                10
            )
        }
    }

    companion object {
        private val warsawEntity = CityEntity(
            id = 1,
            name = "Warsaw",
            country = "PL",
            state = null,
            lat = 52.23,
            lon = 21.01,
            savedAt = 1_000_000L
        )
        private val warsawCity = City("Warsaw", "PL", 52.23, 21.01, state = null)
    }
}
