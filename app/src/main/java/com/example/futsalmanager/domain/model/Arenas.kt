package com.example.futsalmanager.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Arenas(
    val address: String? = null,
    val availability: Availability? = Availability(),
    val arenaType: String? = null,
    val city: String? = null,
    val country: String? = null,
    val courtCount: Int? = null,
    val currency: String? = null,
    val distance: JsonElement,
    val id: String? = null,
    val latitude: Double? = 0.0,
    val logoUrl: String? = null,
    val longitude: Double? = 0.0,
    val name: String? = null,
    val primaryColor: String? = null,
    val subdomain: String? = null,
    val timezone: String? = null
)