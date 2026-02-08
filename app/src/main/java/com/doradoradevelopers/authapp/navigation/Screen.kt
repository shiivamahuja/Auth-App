package com.doradoradevelopers.authapp.navigation


sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Login : Screen("login")

    object Home : Screen("home/{user}") {
        fun createRoute(user: String) = "home/$user"
    }
}

