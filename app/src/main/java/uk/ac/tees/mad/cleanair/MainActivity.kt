package uk.ac.tees.mad.cleanair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.cleanair.ui.screens.auth.LoginScreen
import uk.ac.tees.mad.cleanair.ui.screens.auth.SignupScreen
import uk.ac.tees.mad.cleanair.ui.screens.dashboard.DashboardScreen
import uk.ac.tees.mad.cleanair.ui.screens.splash.SplashScreen
import uk.ac.tees.mad.cleanair.ui.theme.CleanAirTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanAirTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("login") { LoginScreen(navController = navController) }
        composable("signup") { SignupScreen(navController = navController) }
        composable("dashboard") { DashboardScreen(navController = navController) }
        // other routes...
    }
}
