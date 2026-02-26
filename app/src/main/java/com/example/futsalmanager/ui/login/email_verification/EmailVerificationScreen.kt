package com.example.futsalmanager.ui.login.email_verification

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.futsalmanager.ui.component.EmailVerificationComponent
import com.example.futsalmanager.ui.login.viewmodels.EmailVerificationViewModel


@Composable
fun EmailVerificationScreenRoute(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = hiltViewModel<EmailVerificationViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { e ->
                when (e) {
                    EmailVerificationEffect.OnBackClicked -> onBack()
                    EmailVerificationEffect.Navigate -> onSubmitted()
                    is EmailVerificationEffect.ShowError -> snackbarHostState.showSnackbar(e.message)
                }
            }
        }

    }

    EmailVerificationComponent(
        state = state,
        onIntent = viewModel::dispatch,
    )
}


