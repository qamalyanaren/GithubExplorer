package qamalyan.aren.domain

import qamalyan.aren.domain.utils.TextSource


sealed interface InfoEvent {
    data class ErrorAlert(
        val title: TextSource? = TextSource.Resource(R.string.error_title),
        val message: TextSource? = TextSource.Resource(R.string.something_went_wrong),
        val btnText: TextSource = TextSource.Resource(R.string.ok)
    ) : InfoEvent
}
