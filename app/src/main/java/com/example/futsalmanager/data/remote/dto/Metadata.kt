package com.example.futsalmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val priceInfo: PriceInfo
)