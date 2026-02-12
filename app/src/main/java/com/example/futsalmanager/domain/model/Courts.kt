package com.example.futsalmanager.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "courts",
    foreignKeys = [
        ForeignKey(
            entity = Arenas::class,
            parentColumns = ["id"],
            childColumns = ["arenaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("arenaId")]
)
@Serializable
data class Courts(
    val amenities: List<String>,
    val basePrice: String,
    val capacity: Int,
    val description: String,
    @PrimaryKey
    val id: String,
    val arenaId: String,
    val name: String,
    val slotDurationMinutes: Int,
    val type: String
)
