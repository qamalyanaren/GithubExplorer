package qamalyan.aren.githubexplorer.common.utils.manager.info_event

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.Flow
import qamalyan.aren.coreui.extension.collectWhenStarted
import qamalyan.aren.domain.InfoEvent

interface InfoEventCollector {

    fun collectInfoEvents(host: Fragment, events: Flow<InfoEvent>) {
        host.collectWhenStarted(events) { event ->
            onEventReceived(host, event)
        }
    }

    fun onEventReceived(host: Fragment, event: InfoEvent)
}

class InfoEventCollectorImpl : InfoEventCollector {

    override fun onEventReceived(host: Fragment, event: InfoEvent) = when (event) {
        is InfoEvent.ErrorAlert -> onErrorAlert(host, event)
    }

    private fun onErrorAlert(host: Fragment, event: InfoEvent.ErrorAlert) {
        MaterialAlertDialogBuilder(host.requireContext())
            .setTitle(event.title.asString(host.requireContext()))
            .setMessage(event.message.asString(host.requireContext()))
            .setPositiveButton(event.btnText.asString(host.requireContext()), null)
            .show()
    }
}