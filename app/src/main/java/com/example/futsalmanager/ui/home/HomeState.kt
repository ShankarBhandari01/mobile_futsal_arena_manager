package com.example.futsalmanager.ui.home

import com.example.futsalmanager.domain.model.Arenas

data class HomeState(
    val isLoading: Boolean = false,
    val search: String = "",
    val date: String = "",
    val arenaList: List<Arenas>? = emptyList(),
    val offset: Int = 0,
    val limit: Int = 10,
    val isLocationEnabled: Boolean = false,
    val isUsingHighAccuracy: Boolean = false
)
