package com.example.futsalmanager.ui.login.password_reset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.futsalmanager.ui.component.LoadingButton
import com.example.futsalmanager.ui.component.OtpInputField
import com.example.futsalmanager.ui.login.viewmodels.OtpPasswordResetViewModel

@Composable
fun PasswordResetScreen(
    modifier: Modifier = Modifier,
    state: OtpPasswordResetState,
    onIntent: (OtpPasswordResetIntent) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center
        )
        {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onIntent(OtpPasswordResetIntent.OnBackClicked) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text("Back", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Enter the code sent to your email",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = buildAnnotatedString {
                    append(
                        "Code sent to "
                    )
                    withStyle(
                        style =
                            SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append(state.email)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "VERIFICATION CODE",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            OtpInputField(
                otpLength = 6,
                value = state.otp,
                onValueChange = {
                    onIntent(OtpPasswordResetIntent.OnOtpChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "NEW PASSWORD",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                placeholder = {
                    Text("At least 8 characters")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon",
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                value = state.password,
                onValueChange = {
                    onIntent(OtpPasswordResetIntent.PasswordChanged(it))
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff
                    IconButton(
                        onClick =
                            { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle Password"
                        )
                    }
                })
            Spacer(
                modifier = Modifier.height(32.dp)
            )
            Text(
                text = "CONFIRM PASSWORD",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                placeholder = {
                    Text("Confirm your password here.")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon",
                        tint = Color.Gray
                    )
                },
                singleLine = true,
                value = state.confirmPassword,
                onValueChange = {
                    onIntent(OtpPasswordResetIntent.ConfirmPasswordChanged(it))
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff
                    IconButton(
                        onClick =
                            {
                                passwordVisible = !passwordVisible
                            }
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = "Toggle Password"
                        )
                    }
                })
            Spacer(
                modifier = Modifier.height(32.dp)
            )

            LoadingButton(
                onClick = { onIntent(OtpPasswordResetIntent.SubmitClicked) },
                loading = state.loading,
                text = "Reset Password",
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

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

    PasswordResetScreen(
        state = state,
        onIntent = viewModel::dispatch
    )

}

@Preview(showBackground = true)
@Composable
fun PasswordResetScreenPreview() {
    PasswordResetScreen(
        state = OtpPasswordResetState(),
        onIntent = {}
    )
}