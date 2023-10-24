package qamalyan.aren.coreui.extension

import android.content.res.Resources

val Int.px: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Int.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

val Float.px: Float
    get() = this / Resources.getSystem().displayMetrics.density

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density