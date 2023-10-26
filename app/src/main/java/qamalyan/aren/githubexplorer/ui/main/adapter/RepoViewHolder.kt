package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.githubexplorer.common.utils.RString
import qamalyan.aren.githubexplorer.databinding.ItemRepoBinding

class RepoViewHolder(
    private val binding: ItemRepoBinding,
    onItemClicked: (repo: RepoUiEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var repoEntity: RepoUiEntity? = null

    init {
        binding.root.setOnClickListener {
            repoEntity?.let {
                onItemClicked.invoke(it)
            }
        }
    }

    fun bind(repo: RepoUiEntity?) = with(binding) {
        if (repo == null) {
            val resources = itemView.resources
            tvName.text = resources.getString(RString.repo_item_loading)
            tvDescription.isVisible = false
            tvStarsCount.text = resources.getString(RString.repo_item_unknown)
        } else {
            repoEntity = repo
            tvName.text = repo.name
            tvDescription.isVisible = repo.description.isNotBlank()
            tvDescription.text = repo.description
            tvStarsCount.text = repo.starsCount.toString()
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClicked: (repo: RepoUiEntity) -> Unit): RepoViewHolder {
            val binding = ItemRepoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return RepoViewHolder(binding, onItemClicked)
        }
    }
}
