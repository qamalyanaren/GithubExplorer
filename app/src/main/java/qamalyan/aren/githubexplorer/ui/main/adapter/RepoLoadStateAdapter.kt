package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class RepoLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ReposLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ReposLoadStateViewHolder {
        return ReposLoadStateViewHolder.create(parent, retry)
    }
}
