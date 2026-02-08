package com.doradoradevelopers.authapp.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doradoradevelopers.authapp.data.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isOtpSent: Boolean = false,
    val phoneNumber: String = "",
    val countryCode: String = "+91",
    val otp: String = ""
)

sealed class LoginEvent {
    object Success : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()
    private var verificationId: String? = null
    private var timeoutJob: Job? = null

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    val event = MutableSharedFlow<LoginEvent>()

    /* ---------- state updaters ---------- */

    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
    }

    fun updateCountryCode(value: String) {
        _uiState.value = _uiState.value.copy(countryCode = value)
    }

    fun updateOtp(value: String) {
        _uiState.value = _uiState.value.copy(otp = value)
    }

    /* ---------- OTP flow ---------- */

    fun sendOtp(phone: String, activity: Activity) {
        timeoutJob?.cancel()

        _uiState.value = _uiState.value.copy(isLoading = true)

        timeoutJob = viewModelScope.launch {
            delay(15000)
            if (_uiState.value.isLoading) {
                resetUiState()
                event.emit(LoginEvent.Error("Connection timed out. Please try again."))
            }
        }

        repo.sendOtp(
            phoneNumber = phone,
            activity = activity,
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    timeoutJob?.cancel()
                    signIn(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    timeoutJob?.cancel()
                    resetUiState()
                    viewModelScope.launch {
                        event.emit(LoginEvent.Error(e.localizedMessage ?: "Verification failed"))
                    }
                }

                override fun onCodeSent(
                    id: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    timeoutJob?.cancel()
                    verificationId = id
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isOtpSent = true
                    )
                }
            }
        )
    }

    fun verifyOtp(otp: String) {
        val id = verificationId ?: return
        _uiState.value = _uiState.value.copy(isLoading = true)
        val credential = PhoneAuthProvider.getCredential(id, otp)
        signIn(credential)
    }

    private fun signIn(credential: PhoneAuthCredential) {
        repo.signInWithCredential(credential) { success, error ->
            if (success) {
                viewModelScope.launch {
                    _uiState.value = LoginUiState()
                    event.emit(LoginEvent.Success)
                }
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
                viewModelScope.launch {
                    event.emit(LoginEvent.Error(error ?: "Invalid OTP"))
                }
            }
        }
    }

    fun resetUiState() {
        timeoutJob?.cancel()
        verificationId = null
        _uiState.value = LoginUiState()
    }
}
