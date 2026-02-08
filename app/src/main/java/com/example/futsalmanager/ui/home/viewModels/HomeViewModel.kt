package com.example.futsalmanager.ui.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsalmanager.domain.model.FilterParams
import com.example.futsalmanager.domain.model.LocationModel
import com.example.futsalmanager.domain.usecase.HomeUseCase
import com.example.futsalmanager.ui.home.HomeEffect
import com.example.futsalmanager.ui.home.HomeIntent
import com.example.futsalmanager.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {

        observeSearchAndFilters()

        useCase.observerLocationStatus
            .onEach { isEnabled ->
                _state.update {
                    it.copy(isLocationEnabled = isEnabled)
                }
            }
            .launchIn(viewModelScope)

        useCase.userLocation
            .distinctUntilChanged()
            .onEach { loc ->
                _state.update {
                    it.copy(location = loc)
                }
            }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchAndFilters() {
        state
            .map {
                FilterParams(
                    query = it.search,
                    date = it.date,
                    location = it.location,
                    isEnabled = it.isLocationEnabled
                )
            }
            .distinctUntilChanged()
            .debounce(500)
            .onEach { params ->
                loadArenaList(
                    query = params.query,
                    date = params.date,
                    location = params.location,
                    isLocationEnabled = params.isEnabled
                )
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ViewModeChanged -> {
                _state.update { it.copy(viewMode = intent.viewMode) }
            }

            is HomeIntent.SearchChanged -> {
                _state.update { it.copy(search = intent.query, offset = 0) }
            }

            is HomeIntent.DateChanged -> {
                _state.update { it.copy(date = intent.date, offset = 0) }
            }

            is HomeIntent.Refresh -> viewModelScope.launch {
                val s = _state.value
                loadArenaList(s.search, s.date, s.location, s.isLocationEnabled)
            }

            is HomeIntent.EnableLocationClicked -> viewModelScope.launch {
                _effect.send(HomeEffect.NavigateToLocationSettings)
            }

            is HomeIntent.LogoutClicked -> _state.update { it.copy(showLogoutDialog = true) }
            is HomeIntent.DismissLogoutDialog -> _state.update {
                it.copy(showLogoutDialog = false)
            }
            // Group navigation intents together
            HomeIntent.MarketPlaceClicked,
            HomeIntent.MyBookingClicked,
            HomeIntent.MyProfileClicked,
            HomeIntent.ConfirmLogout -> handleNavigation(intent)

        }
    }

    private fun handleNavigation(intent: HomeIntent) = viewModelScope.launch {
        val effect = when (intent) {
            HomeIntent.MarketPlaceClicked -> HomeEffect.NavigateToMarketPlace
            HomeIntent.MyBookingClicked -> HomeEffect.NavigateToMyBooking
            HomeIntent.MyProfileClicked -> HomeEffect.NavigateToMyProfile
            HomeIntent.ConfirmLogout -> {
                _state.update { it.copy(showLogoutDialog = false) }
                HomeEffect.NavigateToLogin
            }

            else -> null
        }
        effect?.let {
            _effect.send(it)
        }
    }

    private suspend fun loadArenaList(
        query: String,
        date: String,
        location: LocationModel?,
        isLocationEnabled: Boolean = false
    ) {
        _state.update { it.copy(isLoading = true) }

        useCase.getArenaList(
            search = query,
            offset = _state.value.offset,
            limit = _state.value.limit,
            date = date,
            location = location,
            isGpsEnabled = isLocationEnabled
        ).fold(
            onSuccess = { result ->
                _state.update { it.copy(arenaList = result.arenas) }
            },
            onFailure = { error ->
                _effect.send(HomeEffect.ShowError(error.message ?: "Network Error"))
            }
        )
        _state.update { it.copy(isLoading = false) }
    }
}