package qamalyan.aren.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import qamalyan.aren.data.database.model.RemoteKey
import qamalyan.aren.data.database.model.RepoDbEntity

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoDbEntity>)

    @Query(
        "SELECT * FROM repos WHERE " +
                "name LIKE :queryString OR description LIKE :queryString " +
                "ORDER BY starsCount DESC"
    )
    fun searchInRepos(queryString: String): PagingSource<Int, RepoDbEntity>

    @Query("SELECT * FROM repos WHERE id = :repoId")
    suspend fun fetchRepoById(repoId: Long): RepoDbEntity?

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}