package com.example.futsalmanager.core.ui.sharedComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.futsalmanager.core.ui.states.BannerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMessageBanner(
    state: BannerState,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = state !is BannerState.Hidden,
        enter = slideInVertically(initialOffsetY = { -200 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -200 }) + fadeOut()
    ) {

        val dismissState = rememberSwipeToDismissBoxState(
            SwipeToDismissBoxValue.Settled,
            SwipeToDismissBoxDefaults.positionalThreshold
        )

        LaunchedEffect(state) {
            if (state is BannerState.Hidden) {
                dismissState.reset()
            }
        }

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { /* Keep empty or add a spacer */ },
            modifier = Modifier.statusBarsPadding()
        ) {
            val (containerColor, icon, message) = when (state) {
                is BannerState.Success -> Triple(
                    Color(0xFF2E7D32),
                    Icons.Rounded.CheckCircle,
                    state.message
                )

                is BannerState.Error -> Triple(
                    MaterialTheme.colorScheme.error,
                    Icons.Rounded.Warning,
                    state.message
                )

                BannerState.Hidden -> Triple(Color.Gray, Icons.Rounded.Info, "")
            }

            Card(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = containerColor,
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(message, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}