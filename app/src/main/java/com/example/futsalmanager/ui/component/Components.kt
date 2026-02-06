package com.example.futsalmanager.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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
import coil.compose.AsyncImage
import com.example.futsalmanager.R
import com.example.futsalmanager.core.utils.Common.shimmerEffect
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.ui.login.AuthMode
import com.example.futsalmanager.ui.theme.BrandGreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
    OtpInputField(
        value = "",
        onValueChange = {}
    )
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
    contentColor: Color = Color.White,
    icon: (@Composable () -> Unit)? = null
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
            icon?.invoke()
            Spacer(Modifier.width(8.dp))
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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
                    .padding(horizontal = 1.dp)
                    .focusRequester(focusRequesters[index])
                    .align(Alignment.CenterVertically)
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

@Composable
fun ArenaCard(
    arenas: Arenas,
    onItemClick: (Arenas) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color.Black
        ) // Explicit black border
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- TOP SECTION: Image and Titles ---
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Arena Thumbnail
                AsyncImage(
                    model = arenas.logoUrl,
                    contentDescription = "Thumbnail of ${arenas.name}",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.reshot),
                    error = painterResource(R.drawable.reshot)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = arenas.name!!,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = arenas.id!!,
                            fontSize = 14.sp,
                            softWrap = false,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- MIDDLE SECTION: Location ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = arenas.address!!,
                    fontSize = 16.sp,
                    softWrap = false,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Clip,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTTOM SECTION: Action Link ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Courts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            onClick = { onItemClick(arenas) }
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArenaCardPreview() {
    //ArenaCard(arenas = Arenas())
}

@Preview(showBackground = true)
@Composable
fun FutsalDatePickerPreview() {
    FutsalDatePickerField()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FutsalDatePickerField(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit = {}
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis() - (24 * 60 * 60 * 1000)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(Date(it))
    } ?: ""

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {

            },
            readOnly = true,
            placeholder = { Text("Pick a date") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.LightGray,
                disabledLeadingIconColor = Color.Gray,
                disabledPlaceholderColor = Color.Gray
            )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = BrandGreen)
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = BrandGreen,
                    selectedDayContentColor = Color.White,
                    todayContentColor = BrandGreen,
                    todayDateBorderColor = BrandGreen
                )
            )
        }
    }
}


@Composable
fun ArenaShimmerItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Shimmer
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Title Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Subtitle Shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(14.dp)
                    .shimmerEffect()
            )
        }
    }
}

