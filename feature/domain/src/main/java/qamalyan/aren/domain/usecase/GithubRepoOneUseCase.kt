package qamalyan.aren.domain.usecase

import kotlinx.coroutines.flow.Flow
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.repository.GithubRepository
import javax.inject.Inject


class GithubRepoOneUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(
        repoId: Long,
    ): Flow<RepoDetailsUiEntity> = repository.fetchRepoById(repoId)
}