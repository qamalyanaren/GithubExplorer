package qamalyan.aren.coreui.utils

import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

fun View.setOnSingleClickListener(
    clickType: ClickType = ClickType.SINGLE,
    clickListener: View.OnClickListener?
) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it, clickType))
    } ?: setOnClickListener(null)
}

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    clickType: ClickType
) : View.OnClickListener {
    private val intervalMs = clickType.millis
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({
                    canClick.set(true)
                }, intervalMs)
                clickListener.onClick(v)
            }
        }
    }
}

enum class ClickType(var value: Long = 0) {
    DEFAULT, SINGLE, CUSTOM(value = 0);

    val millis: Long
        get() = when (this) {
            DEFAULT -> 0L
            SINGLE -> 500L
            else -> if (CUSTOM.value < 0L) 0L else CUSTOM.value
        }

    companion object {
        fun createCustom(value: Long): ClickType {
            val type = CUSTOM
            type.value = value
            return type
        }
    }
}