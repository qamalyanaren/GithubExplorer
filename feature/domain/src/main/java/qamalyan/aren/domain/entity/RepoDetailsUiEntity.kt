package qamalyan.aren.domain.entity


data class RepoDetailsUiEntity(
    val id: Long,
    val ownerName: String?,
    val ownerAvatarUrl: String?,

    val name: String,
    val description: String,
    val starsCount: Int,
    val forksCount: Int,
    val language: String,
    val webUrl: String,
) {
    companion object {
        val DEFAULT = RepoDetailsUiEntity(
            id = -1L,
            ownerAvatarUrl = null,
            ownerName = null,
            name = "",
            description = "",
            starsCount = 0,
            forksCount = 0,
            language = "",
            webUrl = ""
        )
    }
}