package com.example.feature_login.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.core_ui.component.sharedComposables.LoadingButton
import com.example.core_ui.component.sharedComposables.TextLabel
import com.example.feature_login.login.AuthIntent
import com.example.feature_login.login.AuthState

@Composable
fun RegisterContent(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                onIntent(AuthIntent.FirstNameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
        Spacer(Modifier.height(16.dp))
        TextLabel("LAST NAME")
        OutlinedTextField(
            placeholder = { Text("Enter your last name here.") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person Icon",
                    tint = Color.Gray
                )
            },
            singleLine = true, value = state.lastName,
            onValueChange = {
                onIntent(AuthIntent.LastNameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )
        Spacer(Modifier.height(16.dp))
        TextLabel("EMAIL")
        OutlinedTextField(
            placeholder = { Text("Enter your email here.") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = Color.Gray
                )
            },
            value = state.email,
            onValueChange = {
                onIntent(AuthIntent.EmailChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )
        Spacer(Modifier.height(16.dp))

        TextLabel("PHONE NUMBER")
        OutlinedTextField(
            placeholder = { Text("+977 98XXXXXXXX") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "phone Icon",
                    tint = Color.Gray
                )
            },

            singleLine = true, value = state.phone,
            onValueChange = {
                onIntent(AuthIntent.PhoneChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
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
                onIntent(AuthIntent.PasswordChanged(it))
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(Modifier.height(16.dp))
        TextLabel("CONFIRM PASSWORD")
        OutlinedTextField(
            placeholder = { Text("Re-enter your password here.") },
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
                onIntent(AuthIntent.ConfirmPasswordChanged(it))
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onIntent(AuthIntent.SubmitClicked)
                }
            ),
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
            onClick = { onIntent(AuthIntent.SubmitClicked) },
            containerColor = MaterialTheme.colorScheme.primary
        )
    }
}

@PreviewScreenSizes
@Composable
fun RegisterComponentPreview() {
    RegisterContent(
        state = AuthState(
            email = "demo@mail.com",
            password = "123456",
            loading = false
        ),
        onIntent = {}
    )
}