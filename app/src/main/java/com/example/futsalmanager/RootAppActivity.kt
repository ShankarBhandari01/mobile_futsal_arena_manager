package com.example.futsalmanager

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core_ui.component.route.Routes
import com.example.core_ui.component.states.LogoutEventBus
import com.example.core_ui.component.theme.FutsalManagerTheme
import com.example.feature_home.home.FutsalHomeScreenRoute
import com.example.feature_home.home.booking.BookingScreenRoute
import com.example.feature_login.login.LoginScreenRoute
import com.example.feature_login.login.email_verification.EmailVerificationScreenRoute
import com.example.feature_login.login.forget_password.ForgotPasswordScreenRoute
import com.example.feature_login.login.password_reset.PasswordResetScreenRoute
import com.example.feature_login.login.viewmodels.LoginRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RootAppActivity : ComponentActivity() {
    private val viewModel: LoginRegisterViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { !viewModel.isReady.value }
        enableEdgeToEdge()
        setContent {
            FutsalManagerTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {

    val viewModel = hiltViewModel<LoginRegisterViewModel>()
    val startDest by viewModel.startDestination.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    startDest?.let { destination ->

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        )
        { padding ->

            NavHost(
                navController = navController,
                startDestination = destination,
                modifier = Modifier.padding(padding),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(250)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(250)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(250)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(250)
                    )
                }
            ) {
                composable(
                    route = Routes.LOGIN_SCREEN
                ) {
                    LoginScreenRoute(
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
                        onLogout = {
                            viewModel.logout()
                            navigateToLoginScreen(navController)
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
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        LogoutEventBus.logoutEvent.collect {
            Log.e("LogoutEventBus", "Logout event received")
            // viewModel.logout(true)
            //navigateToLoginScreen(navController)
        }

    }
}

fun navigateToLoginScreen(navController: NavHostController) {
    navController.navigate(Routes.LOGIN_SCREEN) {
        popUpTo(Routes.HOME_SCREEN)
        {
            inclusive = true
        }
    }
}
