package qamalyan.aren.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import qamalyan.aren.data.database.dao.RemoteKeyDao
import qamalyan.aren.data.database.dao.RepoDao
import qamalyan.aren.data.database.model.RemoteKey
import qamalyan.aren.data.database.model.RepoDbEntity

@Database(
    entities = [
        RepoDbEntity::class,
        RemoteKey::class
    ],
    version = 1,
    exportSchema = true
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun repoDao() : RepoDao
    abstract fun remoteKeyDao() : RemoteKeyDao
}