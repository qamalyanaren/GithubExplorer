package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import qamalyan.aren.domain.entity.RepoUiEntity

class RepoAdapter(
    private val onItemClicked: (repo: RepoUiEntity) -> Unit
) : PagingDataAdapter<RepoUiEntity, RepoViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder =
        RepoViewHolder.create(parent, onItemClicked)

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val uiModel = getItem(position)
        holder.bind(uiModel)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RepoUiEntity>() {
            override fun areItemsTheSame(oldItem: RepoUiEntity, newItem: RepoUiEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RepoUiEntity, newItem: RepoUiEntity): Boolean =
                oldItem == newItem
        }
    }
}