package qamalyan.aren.domain.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

sealed interface DrawableSource {
    class Dynamic(val drawable: Drawable) : DrawableSource
    class Resource(@DrawableRes val resId: Int): DrawableSource

    fun asDrawable(context: Context): Drawable? = when(this) {
        is Dynamic -> drawable
        is Resource -> ContextCompat.getDrawable(context, resId)
        Companion -> null
    }

    companion object : DrawableSource

}