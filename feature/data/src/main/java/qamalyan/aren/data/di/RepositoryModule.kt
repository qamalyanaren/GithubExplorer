package qamalyan.aren.data.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import qamalyan.aren.data.repository.GithubRepositoryImpl
import qamalyan.aren.domain.repository.GithubRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindGithubRepository(impl: GithubRepositoryImpl): GithubRepository
}