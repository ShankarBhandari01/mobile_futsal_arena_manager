package com.example.futsalmanager.ui.login.forget_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.futsalmanager.ui.component.LoadingButton
import com.example.futsalmanager.ui.component.TextLabel
import com.example.futsalmanager.ui.login.viewmodels.ForgetPasswordViewModel

@Composable
fun ForgetPasswordScreen(
    state: ForgetPasswordState,
    onIntent: (ForgetPasswordIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // survive configuration changes
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

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

                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    //  Back
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onIntent(ForgetPasswordIntent.OnBackClicked) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            "Back to login",
                            color = Color.Gray,
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Title
                    Text(
                        "Forgot Password",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Enter your email to receive a password reset code",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(28.dp))

                    // EMAIL
                    TextLabel("EMAIL ADDRESS", modifier.align(Alignment.Start))

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = {
                            onIntent(ForgetPasswordIntent.EmailChanged(it))
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null
                            )
                        },
                        placeholder = { Text("name@example.com") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                onIntent(ForgetPasswordIntent.SubmitClicked)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(28.dp))

                    LoadingButton(
                        text = "Send Reset Code",
                        loading = state.loading,
                        onClick = {
                            keyboardController?.hide()
                            onIntent(ForgetPasswordIntent.SubmitClicked)
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        "We'll send a 6-digit code to your email address",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

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

    ForgetPasswordScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}

@PreviewScreenSizes
@Composable
fun ForgetPasswordScreenPreview() {
    ForgetPasswordScreen(
        state = ForgetPasswordState(
            email = "demo@mail.com",
            loading = false
        ),
        onIntent = {},
    )
}



