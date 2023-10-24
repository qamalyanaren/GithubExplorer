package qamalyan.aren.domain.delegate

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface Event {
    class ErrorEvent(val message: String) : Event
}

@Singleton
class EventDelegate @Inject constructor() {

    private val _eventChannel = MutableSharedFlow<Event>(
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _eventChannel.asSharedFlow()

    suspend fun postEvent(event: Event) {
        _eventChannel.emit(event)
    }

    fun tryPostEvent(event: Event): Boolean {
        return _eventChannel.tryEmit(event)
    }
}