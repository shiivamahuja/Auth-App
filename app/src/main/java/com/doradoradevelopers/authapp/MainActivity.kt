package com.doradoradevelopers.authapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.doradoradevelopers.authapp.ui.theme.AuthAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthAppTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            SplashScreen(navController)
        }
        composable("loginScreen") {
            LoginScreen(navController)
        }
        composable(
            route = "homeScreen/{user}",
            arguments = listOf(
                navArgument("user") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: ""
            Greet()
        }
    }
}