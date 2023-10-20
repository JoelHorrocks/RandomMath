package app.linkbac.fmd

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Profile : Screen("profile", R.string.profile, Icons.Default.School)
    object Settings: Screen("settings", R.string.settings, Icons.Default.Settings)
    object Scratchpad : Screen("scratchpad", R.string.scratchpad, Icons.Default.Draw)
    object Debug : Screen("debug", R.string.debug, Icons.Default.BugReport)
}