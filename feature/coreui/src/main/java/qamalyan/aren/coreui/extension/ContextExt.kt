package qamalyan.aren.coreui.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)


fun Context.showToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.openLink(link: String) = try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
} catch (exception: ActivityNotFoundException) {
    exception.printStackTrace()
}