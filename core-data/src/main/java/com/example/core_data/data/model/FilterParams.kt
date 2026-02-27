package com.example.core_data.data.model

data class FilterParams(
    val query: String,
    val date: String,
    val location: LocationModel?,
    val isEnabled: Boolean
)
