package com.example.futsalmanager.ui.login.email_verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.futsalmanager.ui.component.LoadingButton
import com.example.futsalmanager.ui.component.OtpInputField
import com.example.futsalmanager.ui.login.viewmodels.EmailVerificationViewModel
import com.example.futsalmanager.ui.theme.FutsalManagerTheme


@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    state: EmailVerificationState,
    onIntent: (EmailVerificationIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        // Back Button
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onIntent(EmailVerificationIntent.OnBackClicked) }) {
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
        Spacer(modifier = Modifier.height(48.dp))

        // Header Section
        Text(
            text = "Verify Your Email",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter the verification code sent to your email",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Icon Circle
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color(0xFFE8F5E9), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "We've sent a 6-digit verification code to your email address. Please enter the code below.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field Label
        Text(
            text = "EMAIL ADDRESS",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            placeholder = { Text("name@example.com") },
            value = state.email,
            onValueChange = {
                onIntent(EmailVerificationIntent.EmailChanged(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Verification Code Section
        Text(
            text = "VERIFICATION CODE",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        OtpInputField(
            value = state.code,
            onValueChange = { onIntent(EmailVerificationIntent.CodeChanged(it)) }
        )
        Spacer(modifier = Modifier.height(32.dp))

        LoadingButton(
            text = "Verify Email",
            loading = state.loading,
            onClick = {
                onIntent(EmailVerificationIntent.SubmitClicked)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            icon = {
                Icon(
                    Icons.Default.CheckCircleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Resend Text
        Text(
            text = buildAnnotatedString {
                append("Didn't receive the code? ")
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF81C784), fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Resend")
                }
            }, style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun EmailVerificationScreenRoute(
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val viewModel = hiltViewModel<EmailVerificationViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { e ->
            when (e) {
                EmailVerificationEffect.OnBackClicked -> onBack()
                EmailVerificationEffect.Navigate -> onSubmitted()
                is EmailVerificationEffect.ShowError -> snackbarHostState.showSnackbar(e.message)
            }
        }
    }

    EmailVerificationScreen(
        state = state,
        onIntent = viewModel::dispatch,
    )
}


@PreviewScreenSizes
@Composable
fun GreetingPreview() {
    FutsalManagerTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                email = "demo@mail.com",
                code = "123456",
                loading = false
            ),
            onIntent = {}

        )
    }
}