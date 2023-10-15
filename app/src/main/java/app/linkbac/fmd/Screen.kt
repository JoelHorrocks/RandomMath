package app.linkbac.fmd

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Scratchpad : Screen("scratchpad", R.string.scratchpad, Icons.Default.Draw)
    object Debug : Screen("debug", R.string.debug, Icons.Default.BugReport)
}