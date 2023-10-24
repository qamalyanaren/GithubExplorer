package qamalyan.aren.githubexplorer.ui.main


import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import qamalyan.aren.coreui.delegate.viewBinding
import qamalyan.aren.coreui.extension.collectLatestWhenStarted
import qamalyan.aren.coreui.extension.showToast
import qamalyan.aren.githubexplorer.R
import qamalyan.aren.githubexplorer.common.base.BaseFragment
import qamalyan.aren.githubexplorer.databinding.FragmentRepoSearchBinding
import qamalyan.aren.githubexplorer.ui.RemotePresentationState
import qamalyan.aren.githubexplorer.ui.asRemotePresentationState
import qamalyan.aren.githubexplorer.ui.main.adapter.RepoAdapter
import qamalyan.aren.githubexplorer.ui.main.adapter.RepoLoadStateAdapter

@AndroidEntryPoint
class RepoSearchFragment : BaseFragment<RepoSearchViewModel>(R.layout.fragment_repo_search) {

    override val viewModel: RepoSearchViewModel by viewModels()
    private val binding by viewBinding(FragmentRepoSearchBinding::bind)
    private val adapter by lazy {
        RepoAdapter(
            onItemClicked = {
                viewModel.navigateToRepoDetails(it.id)
            }
        )
    }

    override fun initView(): Unit = with(binding) {
        val header = RepoLoadStateAdapter { adapter.retry() }
        rvRepo.adapter = adapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = RepoLoadStateAdapter { adapter.retry() }
        )

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                etSearch.text?.trim()?.let {
                    if (it.isNotEmpty()) {
                        rvRepo.scrollToPosition(0)
                        viewModel.acceptAction(UiAction.Search(query = it.toString()))
                    }
                }
                true
            } else {
                false
            }
        }
        etSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                etSearch.text?.trim()?.let {
                    if (it.isNotEmpty()) {
                        rvRepo.scrollToPosition(0)
                        viewModel.acceptAction(UiAction.Search(query = it.toString()))
                    }
                }
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            viewModel.state
                .map { it.query }
                .distinctUntilChanged()
                .collect(etSearch::setText)
        }

        btnRetry.setOnClickListener { adapter.retry() }
        rvRepo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) viewModel.acceptAction(UiAction.Scroll(currentQuery = viewModel.state.value.query))
            }
        })
        val notLoading = adapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = viewModel.state
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        collectLatestWhenStarted(viewModel.pagingDataFlow) {
            adapter.submitData(it)
        }


        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) rvRepo.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && adapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.tvEmpty.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                rvRepo.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                pbLoading.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                btnRetry.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    showToast("\uD83D\uDE28 Wooops ${it.error}")
                }
            }
        }
    }


    override fun initObservers() {

    }
}