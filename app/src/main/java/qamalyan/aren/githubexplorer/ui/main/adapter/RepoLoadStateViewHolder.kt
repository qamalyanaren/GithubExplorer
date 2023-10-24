package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import qamalyan.aren.githubexplorer.R
import qamalyan.aren.githubexplorer.databinding.ItemRepoLoadStateFooterBinding


class ReposLoadStateViewHolder(
    private val binding: ItemRepoLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ReposLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo_load_state_footer, parent, false)
            val binding = ItemRepoLoadStateFooterBinding.bind(view)
            return ReposLoadStateViewHolder(binding, retry)
        }
    }
}
