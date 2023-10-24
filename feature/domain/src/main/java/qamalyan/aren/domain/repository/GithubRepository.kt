package qamalyan.aren.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.entity.RepoUiEntity

interface GithubRepository {
    fun searchRepositories(
        query: String
    ): Flow<PagingData<RepoUiEntity>>

    fun fetchRepoById(
        id: Long
    ): Flow<RepoDetailsUiEntity>
}