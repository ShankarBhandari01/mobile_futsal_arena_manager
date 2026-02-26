package com.example.futsalmanager.ui.login.password_reset

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.futsalmanager.ui.component.PasswordResetComponent
import com.example.futsalmanager.ui.login.viewmodels.OtpPasswordResetViewModel



@Composable
fun PasswordResetScreenRoute(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = hiltViewModel<OtpPasswordResetViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->

                when (effect) {
                    OtpPasswordResetEffect.OnBackClicked -> onBack()
                    OtpPasswordResetEffect.Navigate -> onSubmitted()
                    is OtpPasswordResetEffect.ShowError -> {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }

            }
        }
    }

    PasswordResetComponent(
        state = state,
        onIntent = viewModel::dispatch
    )

}

