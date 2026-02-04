package com.example.futsalmanager.ui.login.password_reset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.futsalmanager.ui.component.OtpInputField
import com.example.futsalmanager.ui.component.PasswordField
import com.example.futsalmanager.ui.theme.green

@Composable
fun PasswordResetScreen(
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB)),
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(24.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {

                    // ---------------- BACK ----------------
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }

                        Text("Back", color = Color.Gray)
                    }

                    Spacer(Modifier.height(32.dp))


                    // ---------------- HEADER ----------------
                    Text(
                        "Reset Password",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Enter the code sent to your email",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(Modifier.height(24.dp))


                    // ---------------- EMAIL INFO ----------------
                    Text(
                        buildAnnotatedString {
                            append("Code sent to ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("iamshankarbhandari@gmail.com")
                            }
                        },
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(24.dp))


                    // ---------------- OTP ----------------
                    Text("VERIFICATION CODE", color = Color.Gray)
                    Spacer(Modifier.height(12.dp))

                    OtpInputField(
                        value = "",
                        onValueChange = {}
                    )

                    Spacer(Modifier.height(28.dp))


                    // ---------------- PASSWORD ----------------
                    PasswordField(
                        label = "NEW PASSWORD",
                        value = "",
                        onValueChange = {}
                    )

                    Spacer(Modifier.height(20.dp))

                    PasswordField(
                        label = "CONFIRM PASSWORD",
                        value = "",
                        onValueChange = {}
                    )

                    Spacer(Modifier.height(28.dp))


                    // ---------------- BUTTON ----------------
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = green),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Password", color = Color.White)
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PasswordResetScreenPreview() {
    PasswordResetScreen()
}