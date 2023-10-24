package qamalyan.aren.data.mapper

import qamalyan.aren.data.database.model.RepoDbEntity
import qamalyan.aren.data.entity.RepoEntity
import qamalyan.aren.domain.utils.Mapper


class MapperRepoEntityToDbEntity :
    Mapper<RepoEntity, RepoDbEntity> {
    override fun map(from: RepoEntity): RepoDbEntity {
        return RepoDbEntity(
            id = from.id,
            name = from.name,
            description = from.description,
            starsCount = from.starsCount,
            forksCount = from.forksCount,
            language = from.language.orEmpty(),
            webUrl = from.webUrl.orEmpty(),
            ownerAvatarUrl = from.owner.avatarUrl,
            ownerName = from.owner.login
        )
    }
}