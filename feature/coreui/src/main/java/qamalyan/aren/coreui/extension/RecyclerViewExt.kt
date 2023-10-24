package qamalyan.aren.coreui.extension

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

fun RecyclerViewHolder<Any>.getString(@StringRes resId: Int, vararg args: Any): String =
    itemView.context.getString(resId, *args)

fun RecyclerViewHolder<Any>.getDrawable(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(itemView.context, resId)

fun RecyclerViewHolder<Any>.getColor(@ColorRes resId: Int) =
    ContextCompat.getColor(itemView.context, resId)