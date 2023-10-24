package qamalyan.aren.domain.entity


data class RepoDetailsUiEntity(
    val id: Long,
    val name: String,
    val description: String,
    val starsCount: Int,
    val webUrl: String,
)