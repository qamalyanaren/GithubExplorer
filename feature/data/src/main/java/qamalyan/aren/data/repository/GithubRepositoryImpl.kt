package qamalyan.aren.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import qamalyan.aren.data.database.GithubDatabase
import qamalyan.aren.data.mapper.MapperRepoDbEntityToDetailsUiEntity
import qamalyan.aren.data.mapper.MapperRepoDbEntityToUiEntity
import qamalyan.aren.data.network.api.GithubApi
import qamalyan.aren.data.repository.mediator.SearchRepoMediator
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.repository.GithubRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepositoryImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val database: GithubDatabase,
) : GithubRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun searchRepositories(query: String): Flow<PagingData<RepoUiEntity>> {


        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = SearchRepoMediator(
                query = query,
                githubApi = githubApi,
                database = database
            ),
            pagingSourceFactory = {
                // appending '%' so we can allow other characters to be before and after the query string
                val dbQuery = "%${query.replace(' ', '%')}%"
                database.repoDao().searchInRepos(dbQuery)
            }
        ).flow
            .map {
                it.map { item ->
                    MapperRepoDbEntityToUiEntity().map(item)
                }
            }
    }

    override fun fetchRepoById(id: Long): Flow<RepoDetailsUiEntity> = flow {
        val repoDbEntity = database.repoDao().fetchRepoById(id)
            ?: throw RuntimeException("Repo with: $id id not found")
        emit(MapperRepoDbEntityToDetailsUiEntity().map(repoDbEntity))
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}