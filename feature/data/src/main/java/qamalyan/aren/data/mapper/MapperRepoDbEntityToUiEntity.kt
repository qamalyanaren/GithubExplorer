package qamalyan.aren.data.mapper

import qamalyan.aren.data.database.model.RepoDbEntity
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.domain.utils.Mapper


class MapperRepoDbEntityToUiEntity :
    Mapper<RepoDbEntity, RepoUiEntity> {
    override fun map(from: RepoDbEntity): RepoUiEntity {
        return RepoUiEntity(
            id = from.id,
            name = from.name,
            description = from.description.orEmpty(),
            starsCount = from.starsCount,
        )
    }
}