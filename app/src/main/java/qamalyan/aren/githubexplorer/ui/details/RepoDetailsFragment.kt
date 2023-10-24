package qamalyan.aren.githubexplorer.ui.details


import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qamalyan.aren.coreui.delegate.viewBinding
import qamalyan.aren.coreui.extension.collectWhenStarted
import qamalyan.aren.githubexplorer.R
import qamalyan.aren.githubexplorer.common.base.BaseFragment
import qamalyan.aren.githubexplorer.databinding.FragmentRepoDetailsBinding

@AndroidEntryPoint
class RepoDetailsFragment : BaseFragment<RepoDetailsViewModel>(R.layout.fragment_repo_details) {

    override val viewModel: RepoDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentRepoDetailsBinding::bind)

    override fun initView(): Unit = with(binding) {

    }


    override fun initObservers() {
        collectWhenStarted(viewModel.repoDetailsEntity) { repoEntity ->
            binding.tvName.text = repoEntity.name
        }
    }
}