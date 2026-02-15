package com.example.futsalmanager.ui

import com.example.futsalmanager.ui.home.FutsalHomeScreenRoute
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.futsalmanager.ui.home.booking.BookingScreenRoute
import com.example.futsalmanager.ui.login.LoginScreenRoute
import com.example.futsalmanager.ui.login.email_verification.EmailVerificationScreenRoute
import com.example.futsalmanager.ui.login.forget_password.ForgotPasswordScreenRoute
import com.example.futsalmanager.ui.login.password_reset.PasswordResetScreenRoute
import com.example.futsalmanager.ui.login.viewmodels.LoginRegisterViewModel
import com.example.futsalmanager.ui.routes.Routes
import com.example.futsalmanager.ui.theme.FutsalManagerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RootAppActivity : ComponentActivity() {
    private val viewModel: LoginRegisterViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !viewModel.isReady.value
        }

        enableEdgeToEdge()
        setContent {
            FutsalManagerTheme {
                AppRoot()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {

    val viewModel = hiltViewModel<LoginRegisterViewModel>()
    val startDest by viewModel.startDestination.collectAsStateWithLifecycle()

    startDest?.let { destination ->

        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        )
        { padding ->

            NavHost(
                navController = navController,
                startDestination = destination,
                modifier = Modifier
                    .padding(paddingValues = padding)
            ) {
                composable(
                    route = Routes.LOGIN_SCREEN
                ) {
                    LoginScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onNavigateToHome = {
                            navController.navigate(Routes.HOME_SCREEN) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
                composable(
                    route = Routes.FORGOT_PASSWORD_SCREEN,
                ) {
                    ForgotPasswordScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onBack = { navController.popBackStack() },
                        onSubmitted = { email ->
                            navController.navigate(
                                Routes.passwordResetScreen(email)
                            )
                        }
                    )
                }
                composable(
                    route = Routes.PASSWORD_RESET_SCREEN,
                    arguments = listOf(navArgument("email") {
                        type = NavType.StringType
                    })
                ) {
                    PasswordResetScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onBack = { navController.popBackStack() },
                        onSubmitted = {
                            TODO()
                        }

                    )
                }
                composable(
                    route = Routes.EMAIL_VERIFY_SCREEN
                ) {
                    EmailVerificationScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onBack = { navController.popBackStack() },
                        onSubmitted = {
                            navController.navigate(Routes.HOME_SCREEN) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(
                    route = Routes.HOME_SCREEN
                ) {
                    FutsalHomeScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onLogout = {
                            viewModel.logout()
                            navController.navigate(Routes.LOGIN_SCREEN) {
                                popUpTo(Routes.HOME_SCREEN)
                                {
                                    inclusive = true
                                }
                            }
                        },
                        arenaClicked = { arenaId ->
                            Log.d("arenaId", arenaId)

                            navController.navigate(Routes.bookingScreen(arenaId))
                        }
                    )
                }

                composable(
                    route = Routes.BOOKING,
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType
                    })
                ) {
                    BookingScreenRoute(
                        snackbarHostState = snackbarHostState,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }

}