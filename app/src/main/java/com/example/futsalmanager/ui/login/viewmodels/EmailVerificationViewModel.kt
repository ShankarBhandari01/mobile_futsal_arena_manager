package com.example.futsalmanager.ui.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.domain.usecase.LoginUseCase
import com.example.futsalmanager.ui.login.email_verification.EmailVerificationEffect
import com.example.futsalmanager.ui.login.email_verification.EmailVerificationIntent
import com.example.futsalmanager.ui.login.email_verification.EmailVerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state.asStateFlow()

    private val _effect = Channel<EmailVerificationEffect>()
    val effect = _effect.receiveAsFlow()

    fun dispatch(intent: EmailVerificationIntent) {
        when (intent) {
            is EmailVerificationIntent.EmailChanged -> _state.update { it.copy(email = intent.email) }
            EmailVerificationIntent.OnBackClicked -> viewModelScope.launch {
                _effect.send(EmailVerificationEffect.OnBackClicked)
            }

            is EmailVerificationIntent.CodeChanged -> _state.update { it.copy(code = intent.code) }
            EmailVerificationIntent.SubmitClicked -> submit()
        }
    }

    private fun submit() = viewModelScope.launch {
        val current = _state.value
        if (current.email.isBlank()) {
            _effect.send(EmailVerificationEffect.ShowError("Email cannot be empty"))
            return@launch
        }
        _state.update { it.copy(loading = true) }
        try {

        } catch (e: Exception) {
            _effect.send(EmailVerificationEffect.ShowError(e.message ?: "Unknown error"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }
}

