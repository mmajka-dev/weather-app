package pl.mmajka.weatherapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.mmajka.weatherapp.domain.model.City
import pl.mmajka.weatherapp.domain.model.error.SearchQueryError
import pl.mmajka.weatherapp.domain.usecase.GetSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.SaveSearchHistoryUseCase
import pl.mmajka.weatherapp.domain.usecase.SearchCitiesUseCase

private const val DEBOUNCE_MS = 500L

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchViewState())
    val state: StateFlow<SearchViewState> = _state.asStateFlow()

    private val _navEvent = Channel<NavEvent>(Channel.BUFFERED)
    val navEvent = _navEvent.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        observeHistory()
        observeQuery()
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.QueryChanged -> updateQuery(action.query)
            is UiAction.CitySelected -> handleCitySelected(action.city)
            is UiAction.HistoryEntrySelected -> handleHistoryEntrySelected(action.city)
            UiAction.ClearQuery -> clearQuery()
        }
    }

    private fun clearQuery() {
        searchJob?.cancel()
        _state.update { it.copy(query = "", searchState = SearchState.Idle) }
    }

    private fun updateQuery(query: String) {
        _state.update { it.copy(query = query) }
    }

    private fun observeHistory() {
        getSearchHistoryUseCase()
            .onEach { entries -> _state.update { it.copy(history = entries) } }
            .launchIn(viewModelScope)
    }

    private fun observeQuery() {
        _state
            .map { it.query }
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { query -> triggerSearch(query) }
            .launchIn(viewModelScope)
    }

    private fun triggerSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(searchState = SearchState.Loading) }
            searchCitiesUseCase(query).fold(
                onSuccess = { cities ->
                    _state.update { it.copy(searchState = SearchState.Cities(cities)) }
                },
                onFailure = { error -> _state.update { it.copy(searchState = toSearchState(error)) } }
            )
        }
    }

    private fun toSearchState(error: Throwable): SearchState = when (error) {
        is SearchQueryError.TooShort -> SearchState.Idle
        is SearchQueryError.InvalidCharacters -> SearchState.InvalidQuery
        else -> SearchState.Error(error.message.orEmpty())
    }

    private fun handleCitySelected(city: City) {
        searchJob?.cancel()
        _state.update { it.copy(query = "", searchState = SearchState.Idle) }
        viewModelScope.launch {
            saveSearchHistoryUseCase(city)
            _navEvent.send(NavEvent.NavigateToDetails(city))
        }
    }

    private fun handleHistoryEntrySelected(city: City) {
        searchJob?.cancel()
        _state.update { it.copy(query = "", searchState = SearchState.Idle) }
        viewModelScope.launch { _navEvent.send(NavEvent.NavigateToDetails(city)) }
    }
}
