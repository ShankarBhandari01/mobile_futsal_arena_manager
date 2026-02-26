package com.example.futsalmanager.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsalmanager.domain.model.Arenas
import org.osmdroid.util.GeoPoint

/* ------------------------------------------------ */
/* --------------- BOTTOM SHEET ------------------- */
/* ------------------------------------------------ */

@Composable
fun ArenaDetailSheet(
    arena: Arenas,
    userLocation: GeoPoint?,
    onBookClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {

        Text(
            arena.name ?: "Arena",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(arena.address ?: "", color = Color.Gray)

        userLocation?.let {
            val dest = GeoPoint(arena.latitude ?: 0.0, arena.longitude ?: 0.0)
            val distance = it.distanceToAsDouble(dest) / 1000

            Text(
                "${"%.2f".format(distance)} km away",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val uriString =
                    "google.navigation:q=${arena.latitude},${arena.longitude}"
                val uri = Uri.parse(uriString)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Directions, null)
            Spacer(Modifier.width(8.dp))
            Text("Get Directions")
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                onBookClick()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View & Book Courts")
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
        Spacer(Modifier.height(32.dp))
    }
}
