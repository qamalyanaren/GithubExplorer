package qamalyan.aren.githubexplorer.common.base


import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import qamalyan.aren.coreui.extension.addSystemPadding
import qamalyan.aren.coreui.extension.collectWhenStarted
import qamalyan.aren.coreui.extension.hideKeyboard
import qamalyan.aren.githubexplorer.common.extension.navigateSafe
import qamalyan.aren.githubexplorer.common.navigation.Command
import qamalyan.aren.githubexplorer.common.utils.manager.LightSystemBarsManager
import qamalyan.aren.githubexplorer.common.utils.manager.info_event.InfoEventCollector
import qamalyan.aren.githubexplorer.common.utils.manager.info_event.InfoEventCollectorImpl
import qamalyan.aren.githubexplorer.common.utils.manager.system_padding.SystemPaddingParams


abstract class BaseFragment<VM>(@LayoutRes layout: Int) : Fragment(layout),
    InfoEventCollector by InfoEventCollectorImpl()
        where VM : BaseViewModel {

    abstract val viewModel: VM
    open val applySystemPaddings: Boolean = true
    open val systemPaddingParams: SystemPaddingParams = SystemPaddingParams()
    open val isLightStatusBar = true
    open val isLightNavBar = true

    private var navController: NavController? = null
    protected open val navControllerRes: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applySystemPaddings(view)
        applyLightStatusBar()
        applyLightNavBar()
        navController = getNavController()
        collectWhenStarted(viewModel.command, ::processCommand)
        collectInfoEvents(host = this, viewModel.infoEvent)
        initView()
        initObservers()
    }

    protected open fun initView() {}
    protected open fun initObservers() {}

    protected open fun processCommand(command: Command) {
        when (command) {
            is Command.FinishAppCommand -> activity?.finishAffinity()
            is Command.NavigateUpCommand -> {
                navController?.navigateUp()
            }

            is Command.NavCommand -> {
                navController?.navigateSafe(command.navDirections)
            }

            is Command.NavDeepLinkCommand -> {
                navController?.navigateSafe(command.request)
            }
        }
    }

    private fun getNavController() = try {
        navControllerRes?.let { resId ->
            Navigation.findNavController(requireActivity(), resId)
        } ?: findNavController()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun applySystemPaddings(view: View) {
        if (!applySystemPaddings) return
        view.addSystemPadding(
            isTop = systemPaddingParams.isTop,
            isBottom = systemPaddingParams.isBottom,
            isIncludeIme = systemPaddingParams.isIncludeIme,
        )
    }

    private fun applyLightStatusBar() {
        val manager = activity as? LightSystemBarsManager ?: return
        manager.setUseLightStatusBar(isLightStatusBar)
    }

    private fun applyLightNavBar() {
        val manager = activity as? LightSystemBarsManager ?: return
        manager.setUseLightNavBar(isLightNavBar)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }
}
