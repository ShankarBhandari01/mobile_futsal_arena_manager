package com.example.futsalmanager.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.core.utils.Common.isValidEmail
import com.example.futsalmanager.core.utils.Common.toRegisterRequest
import com.example.futsalmanager.domain.model.User
import com.example.futsalmanager.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val user: StateFlow<User?> = loginUseCase.userFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()
    private val _effect = Channel<AuthEffect>()
    val effect = _effect.receiveAsFlow()

    fun dispatch(intent: AuthIntent) {
        when (intent) {
            AuthIntent.ToggleMode -> {
                _state.update {
                    it.copy(
                        mode = if (it.mode == AuthMode.LOGIN)
                            AuthMode.REGISTER
                        else
                            AuthMode.LOGIN
                    )
                }
            }

            is AuthIntent.EmailChanged ->
                _state.update { it.copy(email = intent.email) }

            is AuthIntent.PasswordChanged ->
                _state.update { it.copy(password = intent.password) }

            is AuthIntent.FirstNameChanged ->
                _state.update { it.copy(firstName = intent.firstname) }

            is AuthIntent.LastNameChanged ->
                _state.update { it.copy(lastName = intent.lastname) }

            is AuthIntent.ConfirmPasswordChanged ->
                _state.update { it.copy(confirmPassword = intent.confirmPassword) }

            is AuthIntent.PhoneChanged ->
                _state.update { it.copy(phone = intent.phone) }


            AuthIntent.SubmitClicked ->
                submit()

            AuthIntent.ForgotPasswordClicked ->
                viewModelScope.launch {
                    _effect.send(AuthEffect.ShowError("Not implemented yet"))
                }

        }
    }

    private fun submit() {
        when (_state.value.mode) {
            AuthMode.LOGIN -> login()
            AuthMode.REGISTER -> register()
        }
    }

    private fun register() = viewModelScope.launch {
        val current = _state.value
        if (current.firstName.isBlank() || current.lastName.isBlank()) {
            _effect.send(AuthEffect.ShowError("First name and last name cannot be empty"))
            return@launch
        } else if (current.email.isBlank()) {
            _effect.send(AuthEffect.ShowError("Email cannot be empty"))
            return@launch
        } else if (!current.email.isValidEmail()) {
            _effect.send(AuthEffect.ShowError("Invalid email"))
            return@launch
        } else if (current.phone.isBlank()) {
            _effect.send(AuthEffect.ShowError("Phone cannot be empty"))
            return@launch
        } else if (current.password.isBlank()) {
            _effect.send(AuthEffect.ShowError("Password cannot be empty"))
            return@launch
        } else if (current.password.length < 8) {
            _effect.send(AuthEffect.ShowError("Password must be at least 8 characters"))
            return@launch
        } else if (current.password != current.confirmPassword) {
            _effect.send(AuthEffect.ShowError("Passwords do not match"))
        }
        _state.update { it.copy(loading = true) }

        try {
            val request = current.toRegisterRequest()
            loginUseCase.register(request).fold(
                onSuccess = {
                    _effect.send(AuthEffect.Navigate)
                },
                onFailure = {
                    _effect.send(
                        AuthEffect.ShowError(
                            it.message ?: "Registration failed"
                        )
                    )
                }
            )
        } catch (e: Exception) {
            _effect.send(AuthEffect.ShowError(e.message ?: "Registration failed"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }

    private fun login() = viewModelScope.launch {
        val current = _state.value

        if (current.email.isBlank() || current.password.isBlank()) {
            _effect.send(AuthEffect.ShowError("Email and password cannot be empty"))
            return@launch
        } else if (!current.email.isValidEmail()) {
            _effect.send(AuthEffect.ShowError("Invalid email"))
            return@launch
        }

        _state.update { it.copy(loading = true) }

        try {
            loginUseCase(current.email, current.password).fold(
                onSuccess = { _effect.send(AuthEffect.Navigate) },
                onFailure = {
                    _effect.send(
                        AuthEffect.ShowError(
                            it.message ?: "Login failed"
                        )
                    )
                }
            )
        } catch (e: Exception) {
            _effect.send(AuthEffect.ShowError(e.message ?: "Login failed"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginUseCase.logout()
        }
    }
}
