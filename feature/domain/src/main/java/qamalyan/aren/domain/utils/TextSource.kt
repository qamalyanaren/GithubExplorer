package qamalyan.aren.domain.utils

import android.content.Context
import androidx.annotation.StringRes

sealed interface TextSource {

    class Dynamic(val value: String) : TextSource
    class Resource(@StringRes val resId: Int, vararg val args: Any) : TextSource

    fun asString(context: Context): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> context.getString(resId, *args)
            Companion -> ""
        }
    }

    companion object : TextSource

}