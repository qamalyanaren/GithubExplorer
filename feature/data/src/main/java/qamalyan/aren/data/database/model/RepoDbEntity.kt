package qamalyan.aren.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "repos",
    indices = [Index("starsCount")],
)
data class RepoDbEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String?,
    val starsCount: Int,
    val forksCount: Int,
    val language: String,
    val webUrl: String,

    val ownerAvatarUrl: String?,
    val ownerName: String?,
)