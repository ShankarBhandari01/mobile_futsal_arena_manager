package com.example.futsalmanager.domain.model

data class FilterParams(
    val query: String,
    val date: String,
    val location: LocationModel?,
    val isEnabled: Boolean
)
