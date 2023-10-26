package qamalyan.aren.githubexplorer.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import qamalyan.aren.githubexplorer.databinding.ItemRepoLoadStateFooterBinding


class ReposLoadStateViewHolder(
    private val binding: ItemRepoLoadStateFooterBinding,
    retryClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener { retryClicked.invoke() }
    }

    fun bind(loadState: LoadState) = with(binding) {
        if (loadState is LoadState.Error) {
            tvErrorMessage.text = loadState.error.localizedMessage
        }
        pbLoading.isVisible = loadState is LoadState.Loading
        btnRetry.isVisible = loadState is LoadState.Error
        tvErrorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retryClicked: () -> Unit): ReposLoadStateViewHolder {
            val binding = ItemRepoLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ReposLoadStateViewHolder(binding, retryClicked)
        }
    }
}
