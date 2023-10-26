package qamalyan.aren.githubexplorer.ui.details


import androidx.core.text.parseAsHtml
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import qamalyan.aren.coreui.delegate.viewBinding
import qamalyan.aren.coreui.extension.collectWhenStarted
import qamalyan.aren.coreui.extension.openLink
import qamalyan.aren.coreui.utils.setOnSingleClickListener
import qamalyan.aren.githubexplorer.R
import qamalyan.aren.githubexplorer.common.base.BaseFragment
import qamalyan.aren.githubexplorer.common.utils.RString
import qamalyan.aren.githubexplorer.databinding.FragmentRepoDetailsBinding

@AndroidEntryPoint
class RepoDetailsFragment : BaseFragment<RepoDetailsViewModel>(R.layout.fragment_repo_details) {

    override val viewModel: RepoDetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentRepoDetailsBinding::bind)

    override fun initView(): Unit = with(binding) {
        ivBack.setOnClickListener { viewModel.navigateUp() }
    }


    override fun initObservers() {
        collectWhenStarted(viewModel.repoDetailsEntity) { repoEntity ->
            with(binding) {
                Glide.with(this@RepoDetailsFragment)
                    .load(repoEntity.ownerAvatarUrl)
                    .into(ivOwnerAvatar)

                tvName.text = repoEntity.name
                tvDescription.text = repoEntity.description.parseAsHtml()
                tvOwnerName.text = repoEntity.ownerName

                tvLanguage.text =
                    resources.getString(RString.repo_details_language, repoEntity.language)
                tvStarsCount.text = "${repoEntity.starsCount}"
                tvForksCount.text = "${repoEntity.forksCount}"

                btnWebUrl.setOnSingleClickListener {
                    openLink(repoEntity.webUrl)
                }
            }
        }
    }
}