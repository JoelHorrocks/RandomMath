package app.linkbac.fmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.linkbac.fmd.screen.Debug
import app.linkbac.fmd.screen.Home
import app.linkbac.fmd.screen.Scratchpad
import app.linkbac.fmd.ui.theme.RandomMathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            RandomMathTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            Home(navController)
                        }
                        composable(Screen.Scratchpad.route + "/{questionId}") {
                            Scratchpad(navController, it.arguments?.getString("questionId"))
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