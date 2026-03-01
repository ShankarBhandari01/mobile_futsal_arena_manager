package com.example.core_domain.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppliedRule(
    val id: String,
    val type: String
)