package com.example.feature_login.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_data.data.model.User
import com.example.core_data.data.remote.api.apiExceptions.ApiException
import com.example.core_data.data.remote.api.apiExceptions.ApiExceptionTypes
import com.example.core_domain.domain.usecase.LoginUseCase
import com.example.core_ui.component.route.Routes
import com.example.core_ui.component.states.AuthMode
import com.example.core_uitls.utils.Common.isValidEmail
import com.example.feature_login.login.AuthEffect
import com.example.feature_login.login.AuthIntent
import com.example.feature_login.login.AuthState
import com.example.feature_login.login.loginUtls.LoginUtils.toRegisterRequest
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
class LoginRegisterViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    // for splash screen
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val token = loginUseCase.getAccessToken()
            _startDestination.value = if (token != null) {
                Routes.HOME_SCREEN
            } else {
                Routes.LOGIN_SCREEN
            }
            _isReady.value = true
        }
    }

    val user: StateFlow<User?> = loginUseCase.userFlow.stateIn(
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
            is AuthIntent.ToggleMode -> {
                _state.update { it.copy(mode = intent.authMode) }
            }

            is AuthIntent.EmailChanged -> _state.update { it.copy(email = intent.email) }

            is AuthIntent.PasswordChanged -> _state.update { it.copy(password = intent.password) }

            is AuthIntent.FirstNameChanged -> _state.update { it.copy(firstName = intent.firstname) }

            is AuthIntent.LastNameChanged -> _state.update { it.copy(lastName = intent.lastname) }

            is AuthIntent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = intent.confirmPassword) }

            is AuthIntent.PhoneChanged -> _state.update { it.copy(phone = intent.phone) }

            AuthIntent.SubmitClicked -> submit()

            AuthIntent.ForgotPasswordClicked -> viewModelScope.launch {
                _effect.send(AuthEffect.NavigateToForgotPassword)
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
            return@launch
        }
        _state.update { it.copy(loading = true) }

        try {
            val request = current.toRegisterRequest()
            loginUseCase.register(request)
                .fold(
                    onSuccess = {
                        _effect.send(AuthEffect.Navigate)
                    }, onFailure = { throwable ->
                        if (throwable is ApiException) {
                            when (throwable.type) {
                                ApiExceptionTypes.EMAIL_NOT_VERIFIED -> {
                                    _effect.send(AuthEffect.NavigateToEmailVerification)
                                    //_effect.send(AuthEffect.ShowError(throwable.message))
                                }

                                ApiExceptionTypes.INVALID_CREDENTIALS -> {
                                    _effect.send(AuthEffect.ShowError(throwable.message))
                                }

                                ApiExceptionTypes.CONFLICT -> {
                                    _effect.send(AuthEffect.ShowError(throwable.message))
                                }

                                null -> {
                                    _effect.send(AuthEffect.ShowError(throwable.message))
                                }

                                else -> {
                                    _effect.send(AuthEffect.ShowError(throwable.message))
                                }
                            }
                        } else {
                            _effect.send(AuthEffect.ShowError(throwable.message ?: "Unknown error"))
                        }
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
            loginUseCase(
                current.email,
                current.password
            ).fold(
                onSuccess = {
                    _effect.send(AuthEffect.Navigate)
                },
                onFailure = { throwable ->
                    if (throwable is ApiException) {
                        when (throwable.type) {
                            ApiExceptionTypes.EMAIL_NOT_VERIFIED -> {
                                _effect.send(AuthEffect.ShowError(throwable.message))
                                _effect.send(AuthEffect.NavigateToEmailVerification)
                            }

                            ApiExceptionTypes.INVALID_CREDENTIALS -> {
                                _effect.send(AuthEffect.ShowError(throwable.message))
                            }

                            null -> {
                                _effect.send(AuthEffect.ShowError(throwable.message))
                            }

                            else -> {}
                        }
                    } else {
                        _effect.send(AuthEffect.ShowError(throwable.message ?: "Unknown error"))
                    }
                }
            )
        } catch (e: Exception) {
            _effect.send(AuthEffect.ShowError(e.message ?: "Login failed"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }


    fun logout(isAutoLogout: Boolean = false) {
        viewModelScope.launch {
            loginUseCase.logout(isAutoLogout)
        }
    }

}