package qamalyan.aren.data.network.api

import qamalyan.aren.data.entity.RepoSearchEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
    ): RepoSearchEntity
}