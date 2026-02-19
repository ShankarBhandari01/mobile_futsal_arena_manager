package com.example.futsalmanager.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.ui.component.GenericSegmentedToggle
import com.example.futsalmanager.ui.component.LoginContent
import com.example.futsalmanager.ui.component.RegisterContent
import com.example.futsalmanager.ui.component.TermsText
import com.example.futsalmanager.ui.login.viewmodels.LoginRegisterViewModel


@Composable
fun LoginScreenRoute(
    snackbarHostState: SnackbarHostState,
    onNavigateToHome: () -> Unit,
    onEmailVerification: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    val viewModel = hiltViewModel<LoginRegisterViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
    // Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { e ->
            when (e) {
                is AuthEffect.NavigateToEmailVerification -> onEmailVerification()
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(e.message)

                is AuthEffect.Navigate -> {
                    onNavigateToHome()
                }

                is AuthEffect.NavigateToForgotPassword -> onNavigateToForgot()
                else -> {

                }
            }
        }
    }
}


@Composable
fun LoginScreen(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {    // Title
                        Text(
                            "Welcome to the Arena",
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item { Spacer(Modifier.height(6.dp)) }
                    item {
                        Text(
                            "Enter your credentials to access your account",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }


                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                    item {
                        // Animated Toggle
                        GenericSegmentedToggle(
                            onOptionSelected = { onIntent(AuthIntent.ToggleMode(it)) },
                            selectedOption = state.mode,
                            options = listOf(AuthMode.LOGIN, AuthMode.REGISTER)
                        )
                    }

                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                    item {
                        // Animated form switch
                        AnimatedContent(
                            targetState = state.mode, label = "form_switch"
                        ) { mode ->
                            when (mode) {
                                AuthMode.LOGIN -> LoginContent(state, onIntent)

                                AuthMode.REGISTER -> RegisterContent(state, onIntent)
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(18.dp))
                    }

                    item {
                        TermsText({}, {})
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(
        state = AuthState(
            email = "demo@mail.com",
            password = "123456",
            loading = false
        ),
        onIntent = {}
    )
}
