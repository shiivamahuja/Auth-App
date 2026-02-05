package com.doradoradevelopers.authapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )

        LaunchedEffect(Unit) {
            delay(2000)

            val auth = Firebase.auth
            val currentUser = auth.currentUser

            if (currentUser != null) {

                val email = currentUser.email ?: ""

                navController.navigate("homeScreen/$email") {
                    popUpTo("splashScreen") { inclusive = true }
                }

            } else {

                navController.navigate("registerScreen") {
                    popUpTo("splashScreen") { inclusive = true }
                }
            }
        }
    }
}