package qamalyan.aren.coreui.extension

import android.text.Editable

fun Editable?.toStringOrEmpty() = this?.toString().orEmpty()