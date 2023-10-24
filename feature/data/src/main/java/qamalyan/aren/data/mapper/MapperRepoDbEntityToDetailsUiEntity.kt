package qamalyan.aren.data.mapper

import qamalyan.aren.data.database.model.RepoDbEntity
import qamalyan.aren.domain.entity.RepoDetailsUiEntity
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.utils.Mapper


class MapperRepoDbEntityToDetailsUiEntity :
    Mapper<RepoDbEntity, RepoDetailsUiEntity> {
    override fun map(from: RepoDbEntity): RepoDetailsUiEntity {
        return RepoDetailsUiEntity(
            id = from.id,
            name = from.name,
            description = from.description.orEmpty(),
            starsCount = from.starsCount,
            forksCount = from.forksCount,
            language = from.language,
            webUrl = from.webUrl,
        )
    }
}