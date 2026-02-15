package com.example.futsalmanager.ui.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.futsalmanager.R
import com.example.futsalmanager.core.utils.Common.shimmerEffect
import com.example.futsalmanager.domain.model.Arenas
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.login.AuthMode
import com.example.futsalmanager.ui.theme.BrandGreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
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

@Deprecated("Use GenericSegmentedToggle instead")
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
fun <T> GenericSegmentedToggle(
    selectedOption: T,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelProvider: (T) -> String = { it.toString() },
    iconProvider: @Composable ((T) -> ImageVector?) = { null } // Optional icon
) {
    require(options.size == 2) {
        "GenericSegmentedToggle requires exactly 2 options"
    }

    val selectedIndex = options.indexOf(selectedOption)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
    )
    {
        val toggleWidth = maxWidth / 2
        val offset by animateDpAsState(
            targetValue = if (selectedIndex == 0) 0.dp else toggleWidth,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "toggle_slide"
        )

        // Sliding pill
        Box(
            modifier = Modifier
                .offset(x = offset)
                .width(toggleWidth)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        )

        Row(Modifier.fillMaxSize()) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                val icon = iconProvider(option)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onOptionSelected(option) },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = labelProvider(option),
                            fontWeight = if (isSelected) FontWeight.Bold
                            else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }
                }
            }
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
    arena: Arenas,
    onItemClick: (Arenas) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick(arena) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // --- TOP SECTION ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = arena.logoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.reshot),
                    error = painterResource(R.drawable.reshot)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = arena.name ?: "Unknown Arena",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "ID: ${arena.id ?: "N/A"}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- MIDDLE SECTION: Location ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = arena.address ?: "Address not available",
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- BOTTOM SECTION: Action ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
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

@Composable
fun LocationSuccessBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            BrandGreen.copy(alpha = 0.2f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.NearMe,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Showing arenas sorted by distance from your location",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun LocationWarningBanner(
    modifier: Modifier = Modifier,
    onIntent: (HomeIntent) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFFFF1B8)
        ),
        modifier = modifier.fillMaxWidth()
    )
    {
        Row(
            modifier = modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.LocationOff,
                contentDescription = null,
                modifier = modifier.size(20.dp)
            )
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = "Enable location to see nearby arenas first",
                modifier = modifier.weight(1f),
                fontSize = 14.sp
            )
            Text(
                text = "Enable",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onIntent(HomeIntent.EnableLocationClicked)
                    }

            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Logout", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("Are you sure you want to log out of Futsal Manager?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Logout", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun EmptyStateComponent(
    modifier: Modifier = Modifier,
    onResetFilters: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Subtle Icon
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(120.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff, // Or a custom soccer ball icon
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Arenas Found",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We couldn't find any futsal courts matching your current filters. Try changing the date or city.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Button
        OutlinedButton(
            onClick = onResetFilters,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Clear Search", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationBannerPreview() {
    LocationPermissionSheet(false, onConfirm = {}, onDismiss = {})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionSheet(
    isPermanentlyDenied: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Color(0xFF58C472).copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF58C472),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = if (isPermanentlyDenied)
                    "Turn on location"
                else
                    "Find nearby arenas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (isPermanentlyDenied) {
                    "Location access is disabled. Enable it in Settings to discover courts near you."
                } else {
                    "We use your location to show the closest futsal arenas and faster booking."
                },
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF58C472)
                )
            ) {
                Text(
                    if (isPermanentlyDenied) "Open Settings" else "Allow Access"
                )
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = onDismiss) {
                Text("Not now")
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePermissionWrapper(
    onPermissionChanged: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    val permissionState = rememberMultiplePermissionsState(permissionsToRequest)
    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var userDismissed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        onPermissionChanged(permissionState.allPermissionsGranted)
    }

    val permanentlyDenied = permissionState.permissions.any {
        !it.status.isGranted && !it.status.shouldShowRationale
    } && hasRequestedPermission

    val locationGranted = permissionState.permissions
        .filter { it.permission.contains("LOCATION") }
        .all { it.status.isGranted }

    Box(Modifier.fillMaxSize()) {
        content()

        if (!locationGranted && !userDismissed) {
            val locationRationale = permissionState.permissions
                .filter { it.permission.contains("LOCATION") }
                .any { it.status.shouldShowRationale }

            val locationPermanentlyDenied = !locationRationale && hasRequestedPermission


            when {
                locationPermanentlyDenied -> {
                    LocationPermissionSheet(
                        isPermanentlyDenied = true,
                        onConfirm = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                            context.startActivity(intent)
                        },
                        onDismiss = { userDismissed = true }
                    )
                }

                else -> {
                    LocationPermissionSheet(
                        isPermanentlyDenied = false,
                        onConfirm = {
                            hasRequestedPermission = true
                            permissionState.launchMultiplePermissionRequest()
                        },
                        onDismiss = { userDismissed = true }
                    )
                }
            }
        }
    }
}

