package qamalyan.aren.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.repository.GithubRepository
import javax.inject.Inject


class GithubRepoSearchUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(
        query: String,
    ): Flow<PagingData<RepoUiEntity>> = repository.searchRepositories(query)
}