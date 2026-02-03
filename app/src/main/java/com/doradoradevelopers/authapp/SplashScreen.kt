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
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null,
            Modifier.size(250.dp))
        LaunchedEffect(Unit) {
            delay(4000)
            navController.navigate("loginScreen") {
                popUpTo("splashScreen") { inclusive = true }
            }
        }
    }
}