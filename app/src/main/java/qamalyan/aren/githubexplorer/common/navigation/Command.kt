package qamalyan.aren.githubexplorer.common.navigation

import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections

sealed interface Command {

    data object FinishAppCommand : Command

    data object NavigateUpCommand : Command

    class NavCommand(
        val navDirections: NavDirections,
    ) : Command

    class NavDeepLinkCommand(
        val request: NavDeepLinkRequest
    ) : Command
}