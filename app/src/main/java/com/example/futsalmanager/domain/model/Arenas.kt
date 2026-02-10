package com.example.futsalmanager.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "arenas")
@Serializable
data class Arenas(
    @PrimaryKey
    val id: String = "",
    val address: String? = null,
    @Embedded(prefix = "avail_")
    val availability: Availability? = Availability(),
    val arenaType: String? = null,
    val city: String? = null,
    val country: String? = null,
    val courtCount: Int? = null,
    val currency: String? = null,
    val distance: Float? = 0.0F,
    val latitude: Double? = 0.0,
    val logoUrl: String? = null,
    val longitude: Double? = 0.0,
    val name: String? = null,
    val primaryColor: String? = null,
    val subdomain: String? = null,
    val timezone: String? = null
)