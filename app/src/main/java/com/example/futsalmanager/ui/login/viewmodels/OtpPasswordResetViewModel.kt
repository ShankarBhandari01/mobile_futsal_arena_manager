package com.example.futsalmanager.ui.login.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.core.utils.Common.isValidPassword
import com.example.futsalmanager.core.utils.Common.toChangePasswordRequest
import com.example.futsalmanager.domain.usecase.LoginUseCase
import com.example.futsalmanager.ui.apiExceptions.ApiException
import com.example.futsalmanager.ui.login.password_reset.OtpPasswordResetEffect
import com.example.futsalmanager.ui.login.password_reset.OtpPasswordResetIntent
import com.example.futsalmanager.ui.login.password_reset.OtpPasswordResetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpPasswordResetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    // getting value from navigation
    private val email = savedStateHandle.get<String>("email") ?: ""

    private val _state = MutableStateFlow(OtpPasswordResetState(email = email))
    val state = _state.asStateFlow()

    private val _effect = Channel<OtpPasswordResetEffect>()
    val effect = _effect.receiveAsFlow()

    fun dispatch(intent: OtpPasswordResetIntent) {
        when (intent) {
            is OtpPasswordResetIntent.OnBackClicked -> viewModelScope.launch {
                _effect.send(OtpPasswordResetEffect.OnBackClicked)
            }

            is OtpPasswordResetIntent.OnOtpChanged -> _state.update { it.copy(otp = intent.otp) }
            is OtpPasswordResetIntent.PasswordChanged -> _state.update { it.copy(password = intent.password) }
            is OtpPasswordResetIntent.ConfirmPasswordChanged -> _state.update {
                it.copy(
                    confirmPassword = intent.confirmPassword
                )
            }

            is OtpPasswordResetIntent.SubmitClicked -> submit()
        }
    }

    private fun submit() = viewModelScope.launch {
        val current = _state.value
        if (current.otp.isBlank()) {
            _effect.send(OtpPasswordResetEffect.ShowError("OTP cannot be empty"))
            return@launch
        } else if (current.password.isBlank()) {
            _effect.send(OtpPasswordResetEffect.ShowError("Password cannot be empty"))
            return@launch
        } else if (current.confirmPassword.isBlank()) {
            _effect.send(OtpPasswordResetEffect.ShowError("Confirm password cannot be empty"))
            return@launch
        } else if (!current.password.isValidPassword()) {
            _effect.send(OtpPasswordResetEffect.ShowError("Password must be at least 8 characters"))
            return@launch
        } else if (current.password != current.confirmPassword) {
            _effect.send(OtpPasswordResetEffect.ShowError("Passwords do not match"))
            return@launch
        } else if (current.otp.length != 6) {
            _effect.send(OtpPasswordResetEffect.ShowError("Invalid OTP"))
            return@launch
        }
        _state.update { it.copy(loading = true) }

        try {
            loginUseCase.resetPassword(
                current
                    .toChangePasswordRequest()
            ).fold(
                onSuccess = {
                    _effect.send(OtpPasswordResetEffect.Navigate)
                },
                onFailure = { throwable ->
                    if (throwable is ApiException) {
                        when (throwable.type) {
                            else -> {
                                _effect.send(OtpPasswordResetEffect.ShowError(throwable.message))
                            }
                        }
                    } else {
                        _effect.send(
                            OtpPasswordResetEffect.ShowError(
                                throwable.message ?: "Unknown error"
                            )
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _effect.send(OtpPasswordResetEffect.ShowError(e.message ?: "Unknown error"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }


}
