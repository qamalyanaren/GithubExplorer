package qamalyan.aren.githubexplorer.common.utils.manager

import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat

interface LightSystemBarsManager {

    val contentWindow: Window?

    fun setUseLightNavBar(isLight: Boolean) {
        val window = contentWindow ?: return
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightNavigationBars = isLight
    }

    fun setUseLightStatusBar(isLight: Boolean) {
        val window = contentWindow ?: return
        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = isLight
    }

}