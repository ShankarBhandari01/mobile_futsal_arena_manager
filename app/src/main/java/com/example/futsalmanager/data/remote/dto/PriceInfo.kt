package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PriceInfo(
    val appliedRule: AppliedRule,
    val isOverridden: Boolean
)