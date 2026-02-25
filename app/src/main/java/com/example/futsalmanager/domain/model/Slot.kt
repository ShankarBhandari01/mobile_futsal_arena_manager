package com.example.futsalmanager.domain.model

import com.example.futsalmanager.domain.model.emum.SlotStatus
import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val end: String,
    val isOverridden: Boolean,
    val price: String,
    val pricingBadge: String,
    val start: String,
    val status: String
){
    val slotStatus: SlotStatus
        get() = try {
            SlotStatus.valueOf(status.uppercase())
        } catch (_: Exception) {
            SlotStatus.UNAVAILABLE
        }
}