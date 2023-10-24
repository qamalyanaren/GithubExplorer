package qamalyan.aren.coreui.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object KeyboardEventHandler {
    private val mIsKeyboardVisible = MutableSharedFlow<Boolean>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    val isKeyboardVisible = mIsKeyboardVisible.asSharedFlow()

    val keyboardCallback: (isVisible: Boolean) -> Unit = {
        mIsKeyboardVisible.replayCache.firstOrNull()?.let { previous ->
            if (previous != it) {
                mIsKeyboardVisible.tryEmit(it)
            }
        } ?: run {
            mIsKeyboardVisible.tryEmit(it)
        }
    }
}