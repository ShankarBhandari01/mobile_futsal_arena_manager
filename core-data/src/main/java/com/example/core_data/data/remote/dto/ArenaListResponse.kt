package com.example.core_data.data.remote.dto

import com.example.core_data.data.model.Arenas
import kotlinx.serialization.Serializable

@Serializable
data class ArenaListResponse(
    val arenas: List<Arenas>? = emptyList(),
    val computedAt: String? = "",
    val hasMore: Boolean? = false,
    val limit: Int? = 0,
    val offset: Int? = 0,
    val searchParams: SearchParams? = SearchParams(),
    val total: Int? = 0
)