package app.linkbac.fmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.linkbac.fmd.screen.Debug
import app.linkbac.fmd.screen.FlaggedQuestions
import app.linkbac.fmd.screen.Home
import app.linkbac.fmd.screen.PastQuestions
import app.linkbac.fmd.screen.Profile
import app.linkbac.fmd.screen.Scratchpad
import app.linkbac.fmd.screen.Settings
import app.linkbac.fmd.screen.Stats
import app.linkbac.fmd.ui.theme.RandomMathTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val items = listOf(
                Screen.Home,
                Screen.Profile,
                Screen.Settings,
            )

            val bottomBarState = remember { (mutableStateOf(true)) }
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            bottomBarState.value = !items.none { it.route == navBackStackEntry?.destination?.route }

            RandomMathTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    bottomBar = {
                        if(bottomBarState.value) {
                            NavigationBar {
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(stringResource(screen.resourceId)) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { it ->
                    Surface(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route
                        ) {
                            composable(Screen.Home.route) {
                                Home(navController)
                            }
                            composable(Screen.Profile.route) {
                                Profile(navController)
                            }
                            composable(Screen.Settings.route) {
                                Settings()
                            }
                            composable(Screen.FlaggedQuestions.route) {
                                FlaggedQuestions(navController)
                            }
                            composable(Screen.PastQuestions.route) {
                                PastQuestions(navController)
                            }
                            composable(Screen.Stats.route) {
                                Stats(navController)
                            }
                            composable(Screen.Scratchpad.route + "/{questionId}") { navBackStackEntry ->
                                Scratchpad(navController, navBackStackEntry.arguments?.getString("questionId"))
                            }
                            composable(Screen.Debug.route) {
                                Debug(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}