package qamalyan.aren.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RepoSearchEntity(
    @SerialName("total_count") val total: Int = 0,
    val items: List<RepoEntity> = emptyList(),
)