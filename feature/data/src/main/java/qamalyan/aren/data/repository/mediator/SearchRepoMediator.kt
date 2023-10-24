package qamalyan.aren.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import qamalyan.aren.data.database.GithubDatabase
import qamalyan.aren.data.database.model.RemoteKey
import qamalyan.aren.data.database.model.RepoDbEntity
import qamalyan.aren.data.mapper.MapperRepoEntityToDbEntity
import qamalyan.aren.data.network.api.GithubApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SearchRepoMediator(
    private val query: String,
    private val githubApi: GithubApi,
    private val database: GithubDatabase
) : RemoteMediator<Int, RepoDbEntity>() {

    private val remoteKeyDao = database.remoteKeyDao()
    private val repoDao = database.repoDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoDbEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

//            val apiQuery = query + IN_QUALIFIER
            val apiQuery = query

            val result = githubApi.searchRepos(
                query = apiQuery,
                page = page,
                itemsPerPage = state.config.pageSize
            )

            val repos = result.items
            val endOfPaginationReached = repos.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    repoDao.clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKey(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                remoteKeyDao.insertAll(keys)
                repoDao.insertAll(MapperRepoEntityToDbEntity().map(repos))
            }

            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RepoDbEntity>): RemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                remoteKeyDao.remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RepoDbEntity>): RemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                remoteKeyDao.remoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RepoDbEntity>
    ): RemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeyDao.remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        // GitHub page API is 1 based: https://developer.github.com/v3/#pagination
        private const val GITHUB_STARTING_PAGE_INDEX = 1
        private const val IN_QUALIFIER = "in:name,description"
    }
}