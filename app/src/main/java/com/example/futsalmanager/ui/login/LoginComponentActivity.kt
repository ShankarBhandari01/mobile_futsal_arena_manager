package com.example.futsalmanager.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.futsalmanager.ui.component.AuthToggle
import com.example.futsalmanager.ui.component.LoadingButton
import com.example.futsalmanager.ui.component.TermsText
import com.example.futsalmanager.ui.component.TextLabel
import com.example.futsalmanager.ui.theme.FutsalManagerTheme
import com.example.futsalmanager.ui.theme.green
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FutsalManagerTheme {
                MyApplicationApp {
                    onNavigateToHome()
                }
            }
        }
    }
}

fun onNavigateToHome() {

}

fun onEmailVerification() {

}


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onNavigateToHome: () -> Unit
) {


    val scrollState = rememberScrollState()
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

    // Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { e ->
            when (e) {
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(e.message)

                is AuthEffect.Navigate -> {
                    snackbarHostState.showSnackbar("Logged in ${user?.firstName}")
                    onNavigateToHome()
                }
            }
        }
    }

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
                            onToggle = { viewModel.dispatch(AuthIntent.ToggleMode) })
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
                                AuthMode.LOGIN -> LoginContent(state, viewModel)

                                AuthMode.REGISTER -> RegisterContent(state, viewModel)
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
fun LoginContent(state: AuthState, viewModel: LoginViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        TextLabel("EMAIL")
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "First Name Icon",
                    tint = Color.Gray
                )
            },
            value = state.email,
            onValueChange = {
                viewModel.dispatch(AuthIntent.EmailChanged(it))
            },
            placeholder = { Text("name@example.com") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            TextLabel("PASSWORD")

            TextButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    keyboardController?.hide()
                    viewModel.dispatch(AuthIntent.ForgotPasswordClicked)
                }
            ) {
                Text("Forgot password?")
            }
        }

        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = "First Name Icon",
                    tint = Color.Gray
                )
            },
            placeholder = { Text("Enter your password here.") },
            value = state.password,

            onValueChange = {
                viewModel.dispatch(AuthIntent.PasswordChanged(it))
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    viewModel.dispatch(AuthIntent.SubmitClicked)
                }
            )
        )

        Spacer(Modifier.height(22.dp))
        LoadingButton(
            text = "Sign In",
            loading = state.loading,
            onClick = { viewModel.dispatch(AuthIntent.SubmitClicked) },
            containerColor = green
        )
    }
}

@Composable
fun RegisterContent(state: AuthState, viewModel: LoginViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        TextLabel("FIRST NAME")
        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            placeholder = { Text("Enter your first name here.") },
            value = state.firstName,
            onValueChange = {
                viewModel.dispatch(AuthIntent.FirstNameChanged(it))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        TextLabel("LAST NAME")
        OutlinedTextField(
            placeholder = { Text("Enter your last name here.") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person Icon",
                    tint = Color.Gray
                )
            }, singleLine = true, value = state.lastName, onValueChange = {
                viewModel.dispatch(AuthIntent.LastNameChanged(it))
            }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        TextLabel("EMAIL")
        OutlinedTextField(
            placeholder = { Text("Enter your email here.") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "EmailIcon",
                    tint = Color.Gray
                )
            }, value = state.email, onValueChange = {
                viewModel.dispatch(AuthIntent.EmailChanged(it))
            }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        TextLabel("PHONE NUMBER")
        OutlinedTextField(
            placeholder = { Text("+977 98XXXXXXXX") }, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "phone Icon",
                    tint = Color.Gray
                )
            }, singleLine = true, value = state.phone, onValueChange = {
                viewModel.dispatch(AuthIntent.PhoneChanged(it))
            }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        TextLabel("PASSWORD")
        OutlinedTextField(
            placeholder = { Text("At least 8 characters") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = "First Name Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            value = state.password,
            onValueChange = {
                viewModel.dispatch(AuthIntent.PasswordChanged(it))
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password")
                }
            },
        )

        Spacer(Modifier.height(16.dp))
        TextLabel("CONFIRM PASSWORD")
        OutlinedTextField(
            placeholder = { Text("Re-enter your password here.") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = "First Name Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            value = state.confirmPassword,
            onValueChange = {
                viewModel.dispatch(AuthIntent.ConfirmPasswordChanged(it))
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password")
                }
            })

        Spacer(Modifier.height(22.dp))

        LoadingButton(
            text = "Create Account",
            loading = state.loading,
            onClick = { viewModel.dispatch(AuthIntent.SubmitClicked) },
            containerColor = green
        )
    }
}

@PreviewScreenSizes
@Composable
fun MyApplicationApp(
    onNavigateToHome: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LoginScreen(
            modifier = Modifier.padding(innerPadding),
            snackbarHostState = snackbarHostState,
            onNavigateToHome = onNavigateToHome
        )
    }
}
