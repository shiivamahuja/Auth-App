package com.doradoradevelopers.authapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doradoradevelopers.authapp.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SplashDestination {
    object Login : SplashDestination()
    data class Home(val email: String) : SplashDestination()
}

class SplashViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination

    init {
        viewModelScope.launch {
            delay(2000)

            val user = repo.getCurrentUser()
            if (user != null) {
                _destination.value =
                    SplashDestination.Home(user.email ?: "")
            } else {
                _destination.value = SplashDestination.Login
            }
        }
    }
}
