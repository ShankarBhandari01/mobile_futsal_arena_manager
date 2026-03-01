package com.example.feature_home.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core_domain.domain.model.emum.PaymentMethod
import com.example.core_ui.component.sharedComposables.BookingHeading
import com.example.feature_home.home.booking.BookingState

@Composable
fun PaymentSection(
    state: BookingState,
    selectedMethod: PaymentMethod,
    onMethodSelected: (PaymentMethod) -> Unit,
    onPaymentButtonClick: () -> Unit = {}
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    val accentColor = MaterialTheme.colorScheme.primary


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(containerColor),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Header Section ---
        BookingHeading(
            "Payment Method",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            step = 3,
            stepCount = 3
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${state.displayStartTime} - ${state.displayEndTime}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = state.selectedCourt?.name ?: "Unknown Court",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$${state.selectedCourt?.basePrice}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
            }
        }

        // Horizontal Selection with spacing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val options = listOf(
                Triple("Online", "Instant", Icons.Default.CreditCard),
                Triple("Cash", "At venue", Icons.Default.Payments),
                Triple("Bank", "Manual", Icons.Default.AccountBalance)
            )

            val methods = PaymentMethod.entries.toTypedArray()

            methods.forEachIndexed { index, method ->
                val (title, sub, icon) = options[index]
                PaymentOptionCard(
                    modifier = Modifier.weight(1f),
                    title = title,
                    subtitle = sub,
                    icon = icon,
                    isSelected = selectedMethod == method,
                    onClick = { onMethodSelected(method) }
                )
            }
        }

        Button(
            onClick = {

                onPaymentButtonClick()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            val buttonText = when (selectedMethod) {
                PaymentMethod.ONLINE -> "Reserve & Pay Online"
                PaymentMethod.CASH -> "Book Now, Pay Later"
                PaymentMethod.BANK -> "Generate Transfer Details"
            }
            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(12.dp))
            Text(
                buttonText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun PaymentOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val secondaryContentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        tonalElevation = if (isSelected) 0.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = secondaryContentColor,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}