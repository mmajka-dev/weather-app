package pl.mmajka.weatherapp.ui.search

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.error.SearchQueryError
import pl.mmajka.weatherapp.domain.usecase.GetSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.SaveSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.SearchCitiesUseCase
import pl.mmajka.weatherapp.util.MainDispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val searchCitiesUseCase: SearchCitiesUseCase = mockk()
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase = mockk()
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase = mockk()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        every { getSearchHistoryUseCase() } returns flowOf(emptyList())
        coEvery { searchCitiesUseCase("") } returns Result.failure(SearchQueryError.TooShort())
        viewModel = SearchViewModel(searchCitiesUseCase, getSearchHistoryUseCase, saveSearchHistoryUseCase)
    }

    @Test
    fun `query is empty and search is idle on startup`() = runTest(mainDispatcherRule.dispatcher) {
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("", state.query)
        assertEquals(SearchState.Idle, state.searchState)
        assertEquals(emptyList<City>(), state.history)
    }

    @Test
    fun `history is loaded from the use case on startup`() = runTest(mainDispatcherRule.dispatcher) {
        every { getSearchHistoryUseCase() } returns flowOf(listOf(warsaw))
        viewModel = SearchViewModel(searchCitiesUseCase, getSearchHistoryUseCase, saveSearchHistoryUseCase)
        advanceUntilIdle()

        assertEquals(listOf(warsaw), viewModel.state.value.history)
    }

    @Test
    fun `query updates immediately when typed`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("War") } returns Result.failure(SearchQueryError.TooShort())

        viewModel.onAction(UiAction.QueryChanged("War"))

        assertEquals("War", viewModel.state.value.query)
    }

    @Test
    fun `search does not fire before debounce elapses`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("War") } returns Result.success(listOf(warsaw))

        viewModel.onAction(UiAction.QueryChanged("War"))
        advanceTimeBy(BEFORE_DEBOUNCE)

        assertEquals(SearchState.Idle, viewModel.state.value.searchState)
    }

    @Test
    fun `search fires after debounce and shows matching cities`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("Warsaw") } returns Result.success(listOf(warsaw))

        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        assertEquals(SearchState.Cities(listOf(warsaw)), viewModel.state.value.searchState)
    }

    @Test
    fun `state is loading while search is in flight`() = runTest(mainDispatcherRule.dispatcher) {
        val deferred = CompletableDeferred<Result<List<City>>>()
        coEvery { searchCitiesUseCase("Warsaw") } coAnswers { deferred.await() }

        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        runCurrent()

        assertEquals(SearchState.Loading, viewModel.state.value.searchState)

        deferred.complete(Result.success(listOf(warsaw)))
        advanceUntilIdle()
    }

    @Test
    fun `too short query leaves state idle`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("W") } returns Result.failure(SearchQueryError.TooShort())

        viewModel.onAction(UiAction.QueryChanged("W"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        assertEquals(SearchState.Idle, viewModel.state.value.searchState)
    }

    @Test
    fun `query with invalid characters sets invalid query state`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("W@r") } returns Result.failure(SearchQueryError.InvalidCharacters())

        viewModel.onAction(UiAction.QueryChanged("W@r"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        assertEquals(SearchState.InvalidQuery, viewModel.state.value.searchState)
    }

    @Test
    fun `network error sets error state with the exception message`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("Warsaw") } returns Result.failure(RuntimeException("No network"))

        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        val state = viewModel.state.value.searchState as SearchState.Error
        assertEquals("No network", state.message)
    }

    @Test
    fun `typing quickly only searches the final query`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("Warsaw") } returns Result.success(listOf(warsaw))

        viewModel.onAction(UiAction.QueryChanged("W"))
        viewModel.onAction(UiAction.QueryChanged("Wa"))
        viewModel.onAction(UiAction.QueryChanged("War"))
        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        coVerify(exactly = 0) { searchCitiesUseCase("W") }
        coVerify(exactly = 0) { searchCitiesUseCase("Wa") }
        coVerify(exactly = 0) { searchCitiesUseCase("War") }
        coVerify(exactly = 1) { searchCitiesUseCase("Warsaw") }
    }

    @Test
    fun `clearing query resets search state to idle`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("Warsaw") } returns Result.success(listOf(warsaw))
        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        viewModel.onAction(UiAction.ClearQuery)

        assertEquals("", viewModel.state.value.query)
        assertEquals(SearchState.Idle, viewModel.state.value.searchState)
    }

    @Test
    fun `selecting a city emits navigate to details event`() = runTest(mainDispatcherRule.dispatcher) {
        coJustRun { saveSearchHistoryUseCase(warsaw) }

        viewModel.navEvent.test {
            viewModel.onAction(UiAction.CitySelected(warsaw))
            advanceUntilIdle()

            assertEquals(NavEvent.NavigateToDetails(warsaw), awaitItem())
        }
    }

    @Test
    fun `selecting a city saves it to history`() = runTest(mainDispatcherRule.dispatcher) {
        coJustRun { saveSearchHistoryUseCase(warsaw) }

        viewModel.onAction(UiAction.CitySelected(warsaw))
        advanceUntilIdle()

        coVerify(exactly = 1) { saveSearchHistoryUseCase(warsaw) }
    }

    @Test
    fun `selecting a city clears the search field`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { searchCitiesUseCase("Warsaw") } returns Result.success(listOf(warsaw))
        coJustRun { saveSearchHistoryUseCase(warsaw) }
        viewModel.onAction(UiAction.QueryChanged("Warsaw"))
        advanceTimeBy(PAST_DEBOUNCE)
        advanceUntilIdle()

        viewModel.onAction(UiAction.CitySelected(warsaw))
        advanceUntilIdle()

        assertEquals("", viewModel.state.value.query)
        assertEquals(SearchState.Idle, viewModel.state.value.searchState)
    }

    @Test
    fun `selecting from history emits navigate to details event`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.navEvent.test {
            viewModel.onAction(UiAction.HistoryEntrySelected(krakow))
            advanceUntilIdle()

            assertEquals(NavEvent.NavigateToDetails(krakow), awaitItem())
        }
    }

    @Test
    fun `selecting from history clears the search field`() = runTest(mainDispatcherRule.dispatcher) {
        viewModel.onAction(UiAction.HistoryEntrySelected(krakow))
        advanceUntilIdle()

        assertEquals("", viewModel.state.value.query)
        assertEquals(SearchState.Idle, viewModel.state.value.searchState)
    }

    companion object {
        private val warsaw = City("Warsaw", "PL", 52.23, 21.01)
        private val krakow = City("Krakow", "PL", 50.06, 19.94)

        private const val PAST_DEBOUNCE = 600L
        private const val BEFORE_DEBOUNCE = 400L
    }
}
