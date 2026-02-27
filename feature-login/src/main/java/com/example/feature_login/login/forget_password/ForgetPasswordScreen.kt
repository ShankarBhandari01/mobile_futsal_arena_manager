package com.example.feature_login.login.forget_password

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.feature_login.login.components.ForgetPasswordComponent
import com.example.feature_login.login.viewmodels.ForgetPasswordViewModel


@Composable
fun ForgotPasswordScreenRoute(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSubmitted: (email: String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = hiltViewModel<ForgetPasswordViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ForgetPasswordEffect.OnBackClicked -> onBack()
                    is ForgetPasswordEffect.NavigateToOtp -> {
                        snackbarHostState.showSnackbar(effect.message)
                        onSubmitted(effect.email)
                    }

                    is ForgetPasswordEffect.ShowError ->
                        snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    ForgetPasswordComponent(
        state = state,
        onIntent = viewModel::dispatch,
    )
}



