package com.example.futsalmanager.core.utils

import android.util.Patterns
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.futsalmanager.data.remote.dto.ChangePasswordRequest
import com.example.futsalmanager.data.remote.dto.RegisterRequest
import com.example.futsalmanager.ui.login.AuthState
import com.example.futsalmanager.ui.login.password_reset.OtpPasswordResetState
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

object Common {

    fun String.isValidEmail(): Boolean {
        return isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun AuthState.toRegisterRequest(): RegisterRequest {
        return RegisterRequest(
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            password = this.password,
            phone = this.phone
        )
    }

    fun String.isValidPassword(): Boolean {
        return this.length >= 8
    }

    fun OtpPasswordResetState.toChangePasswordRequest(): ChangePasswordRequest {
        return ChangePasswordRequest(
            email = this.email,
            otp = this.otp,
            newPassword = this.password
        )
    }

    fun Modifier.shimmerEffect(): Modifier = composed {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        )

        val brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            ),
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )
        background(brush)
    }

    fun formatDateForApi(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            date?.let { outputFormat.format(it) } ?: dateStr
        } catch (e: Exception) {
            dateStr // Fallback to original
        }
    }


    fun String.toDisplayTime(): String {
        if (this.isEmpty()) return ""
        return Instant.parse(this)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun String.toHourInt(): Int {
        return try {
            // Parses the ISO-8601 string (e.g., 2026-02-12T21:00:00Z)
            val instant = Instant.parse(this)
            // Convert to local time zone (e.g., Asia/Kathmandu or system default)
            val dateTime = instant.atZone(ZoneId.systemDefault())
            dateTime.hour // Returns 0-23
        } catch (e: Exception) {
            0 // Fallback
        }
    }

    fun String.toDisplayDate(): String {
        val date = try {
            Instant.parse(this)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } catch (e: Exception) {
            LocalDate.parse(this)
        }

        return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
    }

    fun generateTimeSlots(hoursToAdd: Long = 1): List<LocalTime> {
        val startTime = LocalTime.of(6, 0) // 06:00
        val endTime = LocalTime.of(22, 0)  // 22:00 (10 PM)
        val slots = mutableListOf<LocalTime>()

        var current = startTime
        while (!current.isAfter(endTime)) {
            slots.add(current)
            current = current.plusHours(hoursToAdd)
        }
        return slots
    }

    fun generateIdempotencyKey(): String {
        return UUID.randomUUID().toString()
    }
}
