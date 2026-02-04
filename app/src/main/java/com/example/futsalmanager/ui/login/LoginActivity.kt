package com.example.futsalmanager.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.futsalmanager.ui.component.AuthToggle
import com.example.futsalmanager.ui.component.LoginContent
import com.example.futsalmanager.ui.component.RegisterContent
import com.example.futsalmanager.ui.component.TermsText
import com.example.futsalmanager.ui.login.email_verification.EmailVerificationScreenRoute
import com.example.futsalmanager.ui.login.forget_password.ForgotPasswordScreenRoute
import com.example.futsalmanager.ui.login.viewmodels.LoginRegisterViewModel
import com.example.futsalmanager.ui.routes.Routes
import com.example.futsalmanager.ui.theme.FutsalManagerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FutsalManagerTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun LoginScreenRoute(
    snackbarHostState: SnackbarHostState,
    onNavigateToHome: () -> Unit,
    onEmailVerification: () -> Unit,
    onNavigateToForgot: () -> Unit
) {
    val viewModel = hiltViewModel<LoginRegisterViewModel>()
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

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
                    snackbarHostState.showSnackbar("Logged in ${user?.firstName}")
                    onNavigateToHome()
                }

                is AuthEffect.NavigateToForgotPassword -> onNavigateToForgot()
                else -> {}
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
        color = Color(0xFFF6F7FB)
    ) {
        Box(contentAlignment = Alignment.Center) {

            Card(
                shape = RoundedCornerShape(24.dp),
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
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item { Spacer(Modifier.height(6.dp)) }
                    item {
                        Text(
                            "Enter your credentials to access your account",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }


                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                    item {
                        // Animated Toggle
                        AuthToggle(
                            mode = state.mode,
                            onToggle = { onIntent(AuthIntent.ToggleMode) })
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

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN_SCREEN) {
                LoginScreenRoute(
                    snackbarHostState = snackbarHostState,
                    onNavigateToHome = {
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                        }
                    },
                    onEmailVerification = {
                        navController.navigate(Routes.EMAIL_VERIFY_SCREEN)
                    },
                    onNavigateToForgot = {
                        navController.navigate(Routes.FORGOT_PASSWORD_SCREEN)
                    }
                )
            }
            composable(Routes.FORGOT_PASSWORD_SCREEN) {
                ForgotPasswordScreenRoute(
                    snackbarHostState = snackbarHostState,
                    onBack = { navController.popBackStack() },
                    onSubmitted = {
                        navController.navigate(Routes.EMAIL_VERIFY_SCREEN)
                    }
                )
            }
            composable(Routes.EMAIL_VERIFY_SCREEN) {
                EmailVerificationScreenRoute(
                    snackbarHostState = snackbarHostState,
                    onBack = { navController.popBackStack() },
                    onSubmitted = {
                        navController.navigate(Routes.HOME_SCREEN) {
                            popUpTo(Routes.EMAIL_VERIFY_SCREEN) {
                                inclusive = true
                            }
                        }
                    }
                )
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
