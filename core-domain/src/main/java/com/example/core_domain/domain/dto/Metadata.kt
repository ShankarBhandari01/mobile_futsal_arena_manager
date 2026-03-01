package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val priceInfo: com.example.core_domain.domain.dto.PriceInfo
)