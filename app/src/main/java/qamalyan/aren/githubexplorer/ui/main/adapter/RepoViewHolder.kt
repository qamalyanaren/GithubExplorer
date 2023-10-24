package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import qamalyan.aren.domain.entity.RepoUiEntity
import qamalyan.aren.githubexplorer.R
import qamalyan.aren.githubexplorer.databinding.ItemRepoBinding

/**
 * View Holder for a [RepoUiEntity] RecyclerView list item.
 */
class RepoViewHolder(
    view: View,
    private val onItemClicked: (repo: RepoUiEntity) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRepoBinding.bind(view)

    private var repoEntity: RepoUiEntity? = null

    init {
        view.setOnClickListener {
            repoEntity?.let {
                onItemClicked(it)
            }
        }
    }

    fun bind(repo: RepoUiEntity?) = with(binding) {
        if (repo == null) {
            val resources = itemView.resources
            tvName.text = resources.getString(R.string.loading)
            tvDescription.isVisible = false
            tvStarsCount.text = resources.getString(R.string.unknown)
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
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_repo, parent, false)
            return RepoViewHolder(view, onItemClicked)
        }
    }
}
