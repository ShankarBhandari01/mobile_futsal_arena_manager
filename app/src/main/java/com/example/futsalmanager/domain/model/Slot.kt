package com.example.futsalmanager.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val end: String,
    val isOverridden: Boolean,
    val price: String,
    val pricingBadge: String,
    val start: String,
    val status: String,
    var isSelected: Boolean? = false
)