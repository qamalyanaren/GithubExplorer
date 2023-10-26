package qamalyan.aren.githubexplorer.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.usecase.GithubRepoSearchUseCase
import qamalyan.aren.githubexplorer.common.base.BaseViewModel
import qamalyan.aren.githubexplorer.common.navigation.Command
import qamalyan.aren.githubexplorer.ui.main.RepoSearchViewModel.Companion.DEFAULT_QUERY
import javax.inject.Inject

sealed interface UiAction {
    data class Search(val query: String) : UiAction
    data class Scroll(val currentQuery: String) : UiAction
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepoSearchViewModel @Inject constructor(
    private val githubRepoSearchUseCase: GithubRepoSearchUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val actionStateFlow = MutableSharedFlow<UiAction>()

    val state: StateFlow<UiState>
    val pagingDataFlow: Flow<PagingData<RepoUiEntity>>

    init {
        val initialQuery: String = savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY

        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { githubRepoSearchUseCase(query = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )
    }

    fun acceptAction(action: UiAction) {
        viewModelScope.launch {
            actionStateFlow.emit(action)
        }
    }

    fun navigateToRepoDetails(repoId: Long) {
        val dir = RepoSearchFragmentDirections.actionToRepoDetails(args = repoId)
        sendCommand(Command.NavCommand(dir))
    }


    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }

    companion object {
        const val DEFAULT_QUERY = "Android"
        private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }
}