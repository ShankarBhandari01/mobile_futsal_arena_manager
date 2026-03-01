package com.example.core_domain.domain.dto

import com.example.core_domain.domain.model.Arenas
import kotlinx.serialization.Serializable

@Serializable
data class ArenaListResponse(
    val arenas: List<Arenas>? = emptyList(),
    val computedAt: String? = "",
    val hasMore: Boolean? = false,
    val limit: Int? = 0,
    val offset: Int? = 0,
    val searchParams: com.example.core_domain.domain.dto.SearchParams? = com.example.core_domain.domain.dto.SearchParams(),
    val total: Int? = 0
)