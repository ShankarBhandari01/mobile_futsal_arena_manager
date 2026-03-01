package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class PriceInfo(
    val appliedRule: com.example.core_domain.domain.dto.AppliedRule,
    val isOverridden: Boolean
)