package qamalyan.aren.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import qamalyan.aren.data.database.GithubDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : GithubDatabase {
        return Room.databaseBuilder(
            context,
            GithubDatabase::class.java,
            "github.db"
        )
            .fallbackToDestructiveMigration()
            .enableMultiInstanceInvalidation()
            .build()
    }
}