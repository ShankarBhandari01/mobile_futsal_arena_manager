package com.example.feature_login.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_data.data.remote.api.apiExceptions.ApiException
import com.example.core_domain.domain.usecase.LoginUseCase
import com.example.core_uitls.utils.Common.isValidEmail
import com.example.feature_login.login.forget_password.ForgetPasswordEffect
import com.example.feature_login.login.forget_password.ForgetPasswordIntent
import com.example.feature_login.login.forget_password.ForgetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ForgetPasswordState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ForgetPasswordEffect>()
    val effect = _effect.receiveAsFlow()

    fun dispatch(intent: ForgetPasswordIntent) {
        when (intent) {
            is ForgetPasswordIntent.OnBackClicked -> viewModelScope.launch {
                _effect.send(ForgetPasswordEffect.OnBackClicked)
            }

            is ForgetPasswordIntent.EmailChanged -> _state.update { it.copy(email = intent.email) }

            ForgetPasswordIntent.SubmitClicked -> submit()
        }
    }

    private fun submit() = viewModelScope.launch {
        val current = _state.value
        if (current.email.isBlank()) {
            _effect.send(ForgetPasswordEffect.ShowError("Email cannot be empty"))
            return@launch
        } else if (!current.email.isValidEmail()) {
            _effect.send(ForgetPasswordEffect.ShowError("Invalid email"))
            return@launch
        }
        _state.update { it.copy(loading = true) }
        try {
            loginUseCase.forgotPassword(current.email).fold(
                onSuccess = { res ->
                    _effect.send(
                        ForgetPasswordEffect.NavigateToOtp(
                            current.email.trim(),
                            res.message
                        )
                    )
                },
                onFailure = { throwable ->
                    if (throwable is ApiException) {
                        when (throwable.type) {
                            else -> {
                                _effect.send(ForgetPasswordEffect.ShowError(throwable.message))
                            }
                        }
                    } else {
                        _effect.send(
                            ForgetPasswordEffect.ShowError(
                                throwable.message ?: "Unknown error"
                            )
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _effect.send(ForgetPasswordEffect.ShowError(e.message ?: "Unknown error"))
        } finally {
            _state.update { it.copy(loading = false) }
        }
    }


}