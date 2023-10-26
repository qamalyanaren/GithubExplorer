package qamalyan.aren.githubexplorer.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import qamalyan.aren.domain.usecase.GithubRepoSearchUseCase
import qamalyan.aren.githubexplorer.common.base.BaseViewModel
import qamalyan.aren.githubexplorer.common.navigation.Command
import qamalyan.aren.githubexplorer.ui.main.RepoSearchViewModel.Companion.DEFAULT_QUERY
import javax.inject.Inject

data class SearchRepoUiState(
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

    private val initialQuery by lazy { savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY }
    private val lastQueryScrolled by lazy { savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY }

    private val _queryValue = MutableStateFlow(initialQuery)
    fun setQueryValue(query: String) {
        _queryValue.value = query
    }

    private val _scrollingQueryValue = MutableStateFlow(lastQueryScrolled)
    fun setScrollingQueryValue(scrollingQueryValue: String) {
        _scrollingQueryValue.value = scrollingQueryValue
    }

    val state = combine(
        _queryValue,
        _scrollingQueryValue,
        ::Pair
    ).map { (query, scrollingQuery) ->
        SearchRepoUiState(
            query = query,
            lastQueryScrolled = scrollingQuery,
            // If the search query matches the scroll query, the user has scrolled
            hasNotScrolledForCurrentSearch = query != scrollingQuery
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = SearchRepoUiState()
        )

    val pagingDataFlow =
        _queryValue.flatMapLatest {
            githubRepoSearchUseCase(query = it)
        }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)


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