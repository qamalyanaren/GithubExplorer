package qamalyan.aren.githubexplorer.common.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

@MainThread
fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null) {
        navigate(resId, args, navOptions, navExtras)
    }
}

@MainThread
fun NavController.navigateSafe(directions: NavDirections) {
    navigateSafe(directions.actionId, directions.arguments, null, null)
}

@MainThread
fun NavController.navigateSafe(request: NavDeepLinkRequest) {
    val deepLinkMatch = graph.matchDeepLink(request)
    if (deepLinkMatch != null) {
        navigate(request)
    }
}