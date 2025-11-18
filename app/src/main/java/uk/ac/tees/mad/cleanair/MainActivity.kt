package uk.ac.tees.mad.cleanair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.cleanair.ui.screens.auth.LoginScreen
import uk.ac.tees.mad.cleanair.ui.screens.auth.SignupScreen
import uk.ac.tees.mad.cleanair.ui.screens.splash.SplashScreen
import uk.ac.tees.mad.cleanair.ui.theme.CleanAirTheme

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
        //composable("dashboard") { DashboardScreen(navController = navController) }
        // other routes...
    }
}
