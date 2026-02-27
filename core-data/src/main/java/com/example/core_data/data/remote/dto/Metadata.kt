package com.example.core_data.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val priceInfo: PriceInfo
)