package com.doradoradevelopers.authapp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.doradoradevelopers.authapp.R
import com.doradoradevelopers.authapp.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = viewModel()
) {

    val destination by viewModel.destination.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
    }

    LaunchedEffect(destination) {
        when (val dest = destination) {

            is SplashDestination.Login -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

            is SplashDestination.Home -> {
                navController.navigate(
                    Screen.Home.createRoute(dest.email)
                ) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

            null -> Unit
        }
    }
}
