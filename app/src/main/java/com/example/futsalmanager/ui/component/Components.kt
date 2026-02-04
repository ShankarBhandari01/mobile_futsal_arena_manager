package com.example.futsalmanager.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsalmanager.ui.login.AuthMode

@Composable
fun ToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.surface
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected)
                MaterialTheme.colorScheme.onSurface
            else
                Color.Gray
        )
    }
}


@Composable
fun TermsText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var layoutResult: TextLayoutResult? = null

    val annotated = buildAnnotatedString {
        append("By clicking continue, you agree to our ")

        pushStringAnnotation("TERMS", "terms")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("Terms of Service")
        }
        pop()

        append(" and ")

        pushStringAnnotation("PRIVACY", "privacy")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("Privacy Policy")
        }
        pop()
    }

    Text(
        text = annotated,
        fontSize = 12.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val position = layoutResult
                        ?.getOffsetForPosition(offset)
                        ?: return@detectTapGestures

                    annotated.getStringAnnotations(position, position)
                        .firstOrNull()
                        ?.let {
                            when (it.tag) {
                                "TERMS" -> onTermsClick()
                                "PRIVACY" -> onPrivacyClick()
                            }
                        }
                }
            },
        onTextLayout = { layoutResult = it }
    )
}

@Composable
fun TextLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Gray
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun TogglePreview() {
    ToggleButton(
        text = "Login",
        selected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TermsPreview() {
    TermsText(
        onTermsClick = {},
        onPrivacyClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LabelPreview() {
    TextLabel("EMAIL")
}

@Composable
fun AuthToggle(
    mode: AuthMode,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLogin = mode == AuthMode.LOGIN

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEFEFF2))
    ) {
        val toggleWidth = maxWidth / 2

        val offset by animateDpAsState(
            targetValue = if (isLogin) 0.dp else toggleWidth,
            label = "toggle_slide"
        )

        // sliding pill
        Box(
            modifier = Modifier
                .offset(x = offset)
                .width(toggleWidth)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )

        Row(Modifier.fillMaxSize()) {

            ToggleText(
                text = "Login",
                selected = isLogin,
                onClick = { if (!isLogin) onToggle() },
                modifier = Modifier.weight(1f)
            )

            ToggleText(
                text = "Register",
                selected = !isLogin,
                onClick = { if (isLogin) onToggle() },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToggleText(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.Black else Color.Gray
        )
    }
}

@Composable
fun LoadingButton(
    text: String,
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = Color.Green, // default green
    contentColor: Color = Color.White
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        onClick = {
            // Hide keyboard when clicked
            keyboardController?.hide()
            onClick()
        },
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (loading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    color = contentColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Loading...")
            }
        } else {
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OtpInputField(
    otpLength: Int = 6,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = List(otpLength) { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(otpLength) { index ->
            val char = value.getOrNull(index)?.toString() ?: ""

            OutlinedTextField(
                value = char,
                onValueChange = { input ->
                    val digit = input.filter { it.isDigit() }.take(1)
                    if (digit.isNotEmpty() || char.isNotEmpty()) {
                        val newValue = StringBuilder(value).apply {
                            if (index < otpLength) {
                                if (length > index) this[index] = digit.firstOrNull() ?: ' '
                                else append(digit)
                            }
                        }.toString().trim()
                        onValueChange(newValue)

                        // Move forward if user typed
                        if (digit.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequesters[index])
                    .onKeyEvent { event ->
                        // Handle backspace
                        if (event.key == Key.Backspace) {
                            if (char.isEmpty() && index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                            true
                        } else false
                    },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == otpLength - 1) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )
        }
    }
}


@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    Text(label, color = Color.Gray)
    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation =
            if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    null
                )
            }
        }
    )
}



