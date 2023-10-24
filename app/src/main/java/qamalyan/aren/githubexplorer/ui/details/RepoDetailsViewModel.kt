package qamalyan.aren.githubexplorer.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import qamalyan.aren.domain.InfoEvent
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.usecase.GithubRepoOneUseCase
import qamalyan.aren.domain.utils.TextSource
import qamalyan.aren.githubexplorer.common.base.BaseViewModel
import qamalyan.aren.githubexplorer.common.extension.getOrThrow
import javax.inject.Inject


@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    githubRepoOneUseCase: GithubRepoOneUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _repoDetailsEntity =
        MutableStateFlow(RepoDetailsUiEntity.DEFAULT)
    val repoDetailsEntity = _repoDetailsEntity.asStateFlow()

    private val repoIdArg by lazy { savedStateHandle.getOrThrow<Long>() }

    init {
        githubRepoOneUseCase(repoIdArg)
            .flowOn(Dispatchers.IO)
            .onEach {
                _repoDetailsEntity.value = it
            }
            .catch { throwable ->
                val errorAlert =
                    throwable.message?.let {
                        InfoEvent.ErrorAlert(message = TextSource.Dynamic(it))
                    } ?: run {
                        InfoEvent.ErrorAlert()
                    }
                emitInfoEvent(errorAlert)
            }
            .launchIn(viewModelScope)
    }
}