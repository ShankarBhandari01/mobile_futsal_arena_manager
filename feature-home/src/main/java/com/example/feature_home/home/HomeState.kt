package com.example.feature_home.home

import com.example.core_data.data.model.Arenas
import com.example.core_data.data.model.LocationModel

data class HomeState(
    val viewMode: ViewMode = ViewMode.GRID,
    val isLoading: Boolean = false,
    val search: String = "",
    val date: String = "",
    val arenaList: List<Arenas>? = emptyList(),
    val offset: Int = 0,
    val limit: Int = 10,
    val isLocationEnabled: Boolean = false,
    val isUsingHighAccuracy: Boolean = false,
    val location: LocationModel? = null,
    val showLogoutDialog: Boolean = false,
    val isPermissionGranted: Boolean = false
)
